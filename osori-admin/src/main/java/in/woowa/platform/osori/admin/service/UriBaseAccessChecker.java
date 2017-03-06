package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.data.AccessUri;
import in.woowa.platform.osori.admin.data.resource.AccessResource;
import in.woowa.platform.osori.admin.data.resource.AuthenticationCheckResource;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 27..
 */
@Service
public class UriBaseAccessChecker implements AccessChecker {
    @Autowired
    private MenuNavigationManager menuNavigationManager;

    @Override
    public List<AccessResource> validate(List<MenuNavigation> sourceNavigationGroup, AuthenticationCheckResource checkResource) {
        List<AccessResource> accessResources = new ArrayList<>();

        AntPathMatcher antPathMatcher = new AntPathMatcher();
        for(AccessUri accessUri : checkResource.getAccessUriGroup()){
            boolean isMatch = false;
            String matchedSourceUrl = null;

            for(MenuNavigation menuNavigation : sourceNavigationGroup){
                String sourceUrl = menuNavigationManager.getFullUrl(menuNavigation);
                if(antPathMatcher.match(sourceUrl, accessUri.getUri())
                        && menuNavigation.getMethodType() == accessUri.getRequestMethod()
                        && menuNavigation.getProject().getApiKey().equals(checkResource.getApiKey())){
                    isMatch = true;
                    matchedSourceUrl = sourceUrl;
                    break;
                }
            }

            if(isMatch){
                accessResources.add(AccessResource.ofAccept(matchedSourceUrl));
            }else
                accessResources.add(AccessResource.ofDenied(accessUri.getUri(), "not matched"));

        }

        return accessResources;
    }
}
