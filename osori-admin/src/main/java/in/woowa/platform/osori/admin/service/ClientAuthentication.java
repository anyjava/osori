package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant.AuthenticationCheckType;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.data.resource.AccessResource;
import in.woowa.platform.osori.admin.data.resource.AuthenticationCheckResource;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.manager.ProjectManager;
import in.woowa.platform.osori.admin.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 10. 14..
 * 권한 인증 서비스
 */
@Service
@Transactional
public class ClientAuthentication {

    @Autowired
    private UserManager userManager;
    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private AccessCheckerHandler accessCheckerHandler;

    public boolean isAuthorizedUser(String email){
        User user = userManager.findBy(email);

        if(user == null){
            userManager.createBy(email, "");
            return false;
        }

        return user.getStatus() == User.Status.allow;

    }

    public List<AccessResource> check(AuthenticationCheckResource checkResource){
        Project project = projectManager.findByLiveProjectOfApiKey(checkResource.getApiKey());

        if(project == null)
            throw new ApplicationException(HumanErr.INVALID_APIKEY);

        User user = userManager.findBy(checkResource.getEmail());

        List<MenuNavigation> sourceNavigationGroup = this.getNavigationsOfUser(user, project);

        for(MenuNavigation menuNavigation : sourceNavigationGroup){
            if(!menuNavigation.getProject().equals(project))
                throw new ApplicationException(HumanErr.INVALID_ACCESS);
        }

        AuthenticationCheckType checkType = checkResource.getAuthenticationCheckType();
        AccessChecker accessChecker = accessCheckerHandler.handle(checkType);

        return accessChecker.validate(sourceNavigationGroup, checkResource);
    }

    private List<MenuNavigation> getNavigationsOfUser(User user, Project project){
        List<MenuNavigation> menuNavigations = new ArrayList<>();
        List<AuthorityDefinition> authorityDefinitions = user.getAuthorityDefinitions();

        if(authorityDefinitions.size() == 0)
            throw new ApplicationException(HumanErr.INVALID_ACCESS, "해당 사용자는 권한 설정이 되어있지 않습니다.");

        for(AuthorityDefinition authorityDefinition : authorityDefinitions){
            menuNavigations.addAll(authorityDefinition.getMenuNavigations());
        }

        menuNavigations.addAll(user.getMenuNavigations());

        return project.getMenuNavigations()
                .stream()
                .filter(menuNavigations::contains)
                .collect(Collectors.toList());
    }

}
