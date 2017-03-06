package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.data.resource.AccessResource;
import in.woowa.platform.osori.admin.data.resource.AuthenticationCheckResource;
import in.woowa.platform.osori.admin.entity.MenuNavigation;

import java.util.List;

/**
 * Created by seooseok on 2016. 10. 27..
 * 권한 요청 체크
 */
public interface AccessChecker {

    List<AccessResource> validate(List<MenuNavigation> sourceNavigationGroup, AuthenticationCheckResource authenticationCheckResource);


}
