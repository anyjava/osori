package in.woowa.platform.osori.admin.controller.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by seooseok on 2016. 10. 18..
 */
@Configuration
public class WebViewControllerRegistry extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/view/management/members").setViewName("management/member/members");
        registry.addViewController("/view/management/new-member").setViewName("management/member/new-member");
        registry.addViewController("/view/management/new-member/{userId}/authority-grant").setViewName("management/member/new-member-authority");
        registry.addViewController("/view/management/new-member/{userId}/personal-grant").setViewName("management/member/new-member-personal");
        registry.addViewController("/view/management/new-member/{userId}/complete").setViewName("management/member/complete");

        registry.addViewController("/view/profile").setViewName("profile");
        registry.addViewController("/view/new-project").setViewName("project/new/input-project");
        registry.addViewController("/view/new-project/{projectId}/navi").setViewName("project/new/input-navigation-tree");
        registry.addViewController("/view/new-project/{projectId}/auth-groups").setViewName("project/new/input-authority-groups");
        registry.addViewController("/view/new-project/{projectId}/complete").setViewName("project/new/complete");

        registry.addViewController("/view/project/{projectId}/configuration/navigation").setViewName("project/configuration/navigation");
        registry.addViewController("/view/project/{projectId}/configuration/authority").setViewName("project/configuration/authority-groups");
        registry.addViewController("/view/project/{projectId}/configuration/members").setViewName("project/configuration/members");
    }
}
