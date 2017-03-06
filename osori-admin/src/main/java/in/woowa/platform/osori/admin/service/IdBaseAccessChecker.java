package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.data.resource.AccessResource;
import in.woowa.platform.osori.admin.data.resource.AuthenticationCheckResource;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 27..
 */
@Service
public class IdBaseAccessChecker implements AccessChecker {

    @Autowired
    private MenuNavigationManager menuNavigationManager;

    @Override
    public List<AccessResource> validate(List<MenuNavigation> sourceNavigationGroup, AuthenticationCheckResource checkResource) {
        List<AccessResource> accessResources = new ArrayList<>();

        List<MenuNavigation> targetMenuNavigationGroup = menuNavigationManager.findBy(checkResource.getMenuNavigationIdGroup());

        for(MenuNavigation menuNavigation: targetMenuNavigationGroup){
            if(sourceNavigationGroup.contains(menuNavigation) &&
                    menuNavigation.getProject().getApiKey().equals(checkResource.getApiKey()))
                accessResources.add(AccessResource.ofAccept(menuNavigation.getId()));
            else {
                accessResources.add(AccessResource.ofDenied(menuNavigation.getId(), "접근 할 수 없는 메뉴입니다."));
            }
        }

        return accessResources;



    }
}
