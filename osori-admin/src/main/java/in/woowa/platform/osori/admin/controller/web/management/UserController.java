package in.woowa.platform.osori.admin.controller.web.management;

import com.fasterxml.jackson.annotation.JsonView;
import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.data.JView;
import in.woowa.platform.osori.admin.data.resource.AuthorityGrantResource;
import in.woowa.platform.osori.admin.data.resource.PersonalGrantResource;
import in.woowa.platform.osori.admin.data.resource.UserResource;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.manager.UserManager;
import in.woowa.platform.osori.admin.service.UserGrant;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seooseok on 2016. 10. 18..
 */
@RestController
@RequestMapping("/management/user")
public class UserController {


    @Autowired
    private UserGrant userGrant;
    @Autowired
    private UserManager userManager;

    @PostMapping("")
    public ApiRes create(@Valid @NotEmpty @RequestParam String email
            , @Valid @NotEmpty @RequestParam String name
            , @RequestParam(defaultValue = "") String department
            , @RequestParam boolean isPrivacy){

        User user = userManager.createBy(email, name, department, isPrivacy);
        return new ApiRes(user.getId());
    }


    @JsonView(JView.Detail.class)
    @GetMapping("/{userId}")
    public ApiRes findOne(@PathVariable long userId){
        UserResource userResource = userManager.getUserDetail(userId);

        return new ApiRes(userResource);
    }

    @PutMapping("/{userId}")
    public ApiRes modifyInfo(@PathVariable long userId
            , @RequestParam String name
            , @RequestParam String department
            , @RequestParam boolean isPrivacy){

        User user = userManager.findBy(userId);
        user.setName(name);
        user.setDepartment(department);
        user.setPrivacy(isPrivacy);
        userManager.saveBy(user);

        return ApiRes.success();
    }


    @GetMapping("/{userId}/projects")
    public ApiRes findUsersProjects(@PathVariable long userId){
        UserResource userResource = userManager.getUserDetail(userId);

        return new ApiRes(userResource.getProjectResources());
    }


    @GetMapping("/{userId}/grants")
    public ApiRes findUsersGrants(@PathVariable long userId){

        UserResource userResource = userManager.getUserDetail(userId);

        userResource.getPersonalGrantResources();
        Map<String, Object> map = new HashMap<>();

        map.put("authorityDefinitions",userResource.getAuthorityGrantResources());
        map.put("menuNavigations", userResource.getPersonalGrantResources());

        return new ApiRes(map);
    }


    @PostMapping("/{userId}/grant/project/{projectId}/authority-bundle/{authIdGroup}")
    public ApiRes assignAuthorityGrant(@PathVariable long userId
            , @PathVariable long projectId
            , @PathVariable(value = "authIdGroup") List<Long> authDefinitionIdGroup){

        userGrant.addByAuthorityGrant(userId, projectId, authDefinitionIdGroup);

        return ApiRes.success();
    }


    @DeleteMapping("/{userId}/grant/project/{projectId}/authority-bundle/{authIdGroup}")
    public ApiRes withdrawAuthorityGrant(@PathVariable long userId
            , @PathVariable long projectId
            , @PathVariable(value = "authIdGroup") List<Long> authDefinitionIdGroup){

        userGrant.removeByAuthorityGrant(userId,projectId, authDefinitionIdGroup);

        return ApiRes.success();
    }


    @PostMapping("/{userId}/grant/project/{projectId}/authority-personal/{menuNaviIdGroup}")
    public ApiRes assignPersonalGrant(@PathVariable long userId
            , @PathVariable long projectId
            , @PathVariable(value = "menuNaviIdGroup") List<Long> menuNavigationIdGroup){

        userGrant.addByPersonalGrant(userId, projectId, menuNavigationIdGroup);

        return ApiRes.success();
    }


    @DeleteMapping("/{userId}/grant/project/{projectId}/authority-personal/{menuNaviIdGroup}")
    public ApiRes withdrawPersonalGrant(@PathVariable long userId
            , @PathVariable long projectId
            , @PathVariable(value = "menuNaviIdGroup") List<Long> menuNavigationIdGroup){

        userGrant.removeByPersonalGrant(userId,projectId, menuNavigationIdGroup);
        return ApiRes.success();
    }

    @GetMapping("/{userId}/grant/project/{projectId}")
    public ApiRes findGrantsForUser(@PathVariable long projectId, @PathVariable long userId){
        List<AuthorityGrantResource> authorityGrantResources = userGrant.getUserAuthorityGrants(userId, projectId);
        List<PersonalGrantResource> personalGrantResources = userGrant.getUserPersonalGrants(userId, projectId);

        Map<String, Object> map = new HashMap<>();
        map.put("authorityDefinitions",authorityGrantResources);
        map.put("menuNavigations",personalGrantResources);

        return new ApiRes(map);
    }




}
