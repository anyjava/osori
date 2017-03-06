package in.woowa.platform.osori.admin.controller.api;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.commons.utils.DecryptUtil;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.SystemErr;
import in.woowa.platform.osori.admin.data.AccessUri;
import in.woowa.platform.osori.admin.data.resource.AccessResource;
import in.woowa.platform.osori.admin.data.resource.AccessUriRequest;
import in.woowa.platform.osori.admin.data.resource.AuthenticationCheckResource;
import in.woowa.platform.osori.admin.data.resource.UriBaseAuthorityCheckRequest;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import in.woowa.platform.osori.admin.manager.ProjectManager;
import in.woowa.platform.osori.admin.manager.UserManager;
import in.woowa.platform.osori.admin.service.ClientAuthentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 9. 28..
 */
@RestController
@RequestMapping("/api")
public class AuthorizationController {

    @Autowired
    private ClientAuthentication clientAuthentication;
    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private MenuNavigationManager menuNavigationManager;

    /**
     * @api {post} /user/{email}/authorization-check/id [Id based check]
     * @apiVersion 0.1
     * @apiGroup authorization
     * @apiName 네비ID 권한 체크
     * @apiDescription 오소리 admin에 등록된 메뉴 네비게이션 ID를 가지고 권한을 체크 합니다.
     */
    @PostMapping("/user/{email}/authorization-check/id")
    public ApiRes idChecks(@PathVariable(value = "email") String encryptedEmail, @RequestParam(value = "id") List<Long> idGroup, @RequestParam String apiKey) {
        String email;
        try {
            email = DecryptUtil.aes128(apiKey.substring(0,16), encryptedEmail);
        } catch (GeneralSecurityException e) {
            throw new ApplicationException(SystemErr.ERROR_ENCRYPT);
        }

        if(!clientAuthentication.isAuthorizedUser(email)){
            List<AccessResource> accessResources = new ArrayList<>();

            accessResources.addAll(idGroup.stream()
                    .map(id -> AccessResource.ofDenied(id, "최초 접근한 유저이며 유저의 권한 등록이 필요합니다."))
                    .collect(Collectors.toList()));

            return new ApiRes(accessResources);
        }

        AuthenticationCheckResource checkResource = AuthenticationCheckResource.ofIdBase(email, apiKey, idGroup);
        List<AccessResource> accessResources = clientAuthentication.check(checkResource);

        return new ApiRes(accessResources);
    }


    @PostMapping("/user/{email}/authorization-check/uri")
    public ApiRes urlCheck(@PathVariable(value = "email") String encryptedEmail, @Valid @RequestBody UriBaseAuthorityCheckRequest checkRequest){
        String email;
        List<AccessUri> accessUris = new ArrayList<>();
        String decryptKey = checkRequest.getApiKey().substring(0,16);

        try {
            email = DecryptUtil.aes128(decryptKey, encryptedEmail);
            URI uri;
            for(AccessUriRequest uriRequest : checkRequest.getAccessUriRequests()){
                uri = new URI(uriRequest.getDecryptedUri(decryptKey));
                accessUris.add(AccessUri.of(uri.getPath(), RequestMethod.valueOf(uriRequest.getMethodType())));
            }

        } catch (GeneralSecurityException e) {
            throw new ApplicationException(SystemErr.ERROR_ENCRYPT);
        } catch (URISyntaxException e) {
            throw new ApplicationException(HumanErr.INVALID_URL);
        }

        if(!clientAuthentication.isAuthorizedUser(email)){
            List<AccessResource> accessResources = new ArrayList<>();

            accessResources.addAll(accessUris.stream()
                    .map(accessUri -> AccessResource.ofDenied(accessUri.getUri(), "최초 접근한 유저이며 유저의 권한 등록이 필요합니다."))
                    .collect(Collectors.toList()));

            return new ApiRes(accessResources);
        }

        AuthenticationCheckResource checkResource = AuthenticationCheckResource.ofUrlBase(email, checkRequest.getApiKey(), accessUris);
        List<AccessResource> accessResources =  clientAuthentication.check(checkResource);

        return new ApiRes(accessResources);
    }

    @GetMapping("/user/{email}/project/{projectId}/urls")
    public ApiRes accessibleList(@PathVariable(value = "email") String encryptedEmail,
                                 @PathVariable(value = "projectId") Long projectId,
                                 @RequestParam String apiKey,
                                 @RequestParam(required = false) OsoriConstant.NavigationType type){
        String email;
        try {
            email = DecryptUtil.aes128(apiKey.substring(0,16), encryptedEmail);
        } catch (GeneralSecurityException e) {
            throw new ApplicationException(SystemErr.ERROR_ENCRYPT);
        }

        User user = userManager.findBy(email);
        if(user == null)
            throw new ApplicationException(HumanErr.INVALID_USER);

        Project project = projectManager.findByLive(projectId);
        if(project == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        if(type != null && !Arrays.asList(OsoriConstant.NavigationType.values()).contains(type))
            throw new ApplicationException(HumanErr.INVALID_ARGS);

        List<MenuNavigation> groupMenuNavigations = user.getAuthorityDefinitions()
                .stream()
                .filter(authorityDefinition -> projectId.equals(authorityDefinition.getProject().getId()))
                .map(AuthorityDefinition::getMenuNavigations)
                .distinct()
                .flatMap(List::stream)
                .collect(Collectors.toList());

        List<MenuNavigation> personalMenuNavigations = user.getMenuNavigations()
                .stream()
                .filter(menuNavigation -> projectId.equals(menuNavigation.getProject().getId()))
                .collect(Collectors.toList());

        if(!groupMenuNavigations.isEmpty() && type != null){
            groupMenuNavigations = groupMenuNavigations
                    .stream()
                    .filter(menuNavigation -> type.equals(menuNavigation.getType()))
                    .collect(Collectors.toList());
        }

        if(!personalMenuNavigations.isEmpty() && type != null){
            personalMenuNavigations = personalMenuNavigations
                    .stream()
                    .filter(menuNavigation -> type.equals(menuNavigation.getType()))
                    .collect(Collectors.toList());
        }

        List<MenuNavigation> menuList = new ArrayList<>();
        menuList.addAll(groupMenuNavigations);
        menuList.addAll(personalMenuNavigations);

        List<AccessUri> fullUrls = menuList
            .stream()
            .distinct()
            .map(m -> AccessUri.of(menuNavigationManager.getFullUrl(m), m.getMethodType()))
            .collect(Collectors.toList());

        return new ApiRes(fullUrls);
    }

}
