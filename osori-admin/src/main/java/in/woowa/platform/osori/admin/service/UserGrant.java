package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.data.resource.AuthorityGrantResource;
import in.woowa.platform.osori.admin.data.resource.PersonalGrantResource;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.entity.UserAuthorityGrant;
import in.woowa.platform.osori.admin.entity.UserPersonalGrant;
import in.woowa.platform.osori.admin.manager.AuthorityDefinitionManager;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import in.woowa.platform.osori.admin.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Transactional
@Service
public class UserGrant {

    @Autowired
    private UserManager userManager;
    @Autowired
    private AuthorityDefinitionManager authorityDefinitionManager;
    @Autowired
    private MenuNavigationManager menuNavigationManager;

    public List<AuthorityGrantResource> getUserAuthorityGrants(long id, long projectId){
        List<AuthorityGrantResource> authorityGrantResources = new ArrayList<>();
        User user = userManager.findBy(id);

        List<UserAuthorityGrant> userAuthorityGrants = user.getUserAuthorityGrants()
                .stream()
                .filter(grant -> grant.getAuthorityDefinition().getProject().getId() == projectId)
                .collect(Collectors.toList());

        authorityGrantResources.addAll(userAuthorityGrants
                .stream()
                .map(grant -> AuthorityGrantResource.of(grant.getAuthorityDefinition(), grant.getCreated()))
                .collect(Collectors.toList()));

        return authorityGrantResources;
    }

    public List<PersonalGrantResource> getUserPersonalGrants(long id, long projectId){
        List<PersonalGrantResource> personalGrantResources = new ArrayList<>();
        User user = userManager.findBy(id);

        List<UserPersonalGrant> userPersonalGrants = user.getUserPersonalGrants()
                .stream()
                .filter(grant -> grant.getMenuNavigation().getProject().getId() == projectId)
                .collect(Collectors.toList());

        personalGrantResources.addAll(userPersonalGrants
                .stream()
                .map(grant -> PersonalGrantResource.of(
                        grant.getMenuNavigation(),
                        grant.getCreated(),
                        menuNavigationManager.getFullUrl(grant.getMenuNavigation())
                    )
                )
                .collect(Collectors.toList()));

        return personalGrantResources;
    }

    public void addByAuthorityGrant(long userId, long projectId, List<Long> authorityDefinitionIdGroup){
        User user = userManager.findByStatusAllow(userId);

        List<AuthorityDefinition> authorityDefinitions = authorityDefinitionManager.findByLive(authorityDefinitionIdGroup);


        for(AuthorityDefinition authorityDefinition : authorityDefinitions){
            if(authorityDefinition.getProject().getId() != projectId)
                throw new ApplicationException(HumanErr.INVALID_INCLUDE);

            user.addBy(authorityDefinition);
        }
    }

    public void removeByAuthorityGrant(long userId, long projectId, List<Long> authorityDefinitionIdGroup){
        User user = userManager.findByStatusAllow(userId);

        List<AuthorityDefinition> authorityDefinitions = authorityDefinitionManager.findByLive(authorityDefinitionIdGroup);

        for(AuthorityDefinition authorityDefinition : authorityDefinitions){
            if(authorityDefinition.getProject().getId() != projectId)
                throw new ApplicationException(HumanErr.INVALID_INCLUDE);

            user.removeBy(authorityDefinition);
        }
    }

    public void addByPersonalGrant(long userId, long projectId, List<Long> menuNavigationIdGroup){
        User user = userManager.findByStatusAllow(userId);

        List<MenuNavigation> menuNavigations = menuNavigationManager.findByLive(menuNavigationIdGroup);

        for(MenuNavigation menuNavigation : menuNavigations){
            if(menuNavigation.getProject().getId() != projectId)
                throw new ApplicationException(HumanErr.INVALID_INCLUDE);
            user.addBy(menuNavigation);
        }
    }

    public void removeByPersonalGrant(long userId, long projectId, List<Long> menuNavigationIdGroup){
        User user = userManager.findByStatusAllow(userId);

        List<MenuNavigation> menuNavigations = menuNavigationManager.findByLive(menuNavigationIdGroup);

        for(MenuNavigation menuNavigation : menuNavigations){
            if(menuNavigation.getProject().getId() != projectId)
                throw new ApplicationException(HumanErr.INVALID_INCLUDE);

            user.removeBy(menuNavigation);
        }

    }

}
