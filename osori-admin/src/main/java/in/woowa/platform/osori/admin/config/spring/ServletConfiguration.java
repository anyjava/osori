package in.woowa.platform.osori.admin.config.spring;

import in.woowa.platform.osori.admin.config.filter.CrossScriptingFilter;
import in.woowa.platform.osori.admin.config.filter.UrlBaseLoginFilter;
import in.woowa.platform.osori.admin.config.handler.OsoriLoginHandlerInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by seooseok on 2016. 7. 7..
 * 서블릿 설정
 */
@Configuration
public class ServletConfiguration extends WebMvcConfigurerAdapter {

    @Autowired
    private OsoriLoginHandlerInterceptor osoriLoginHandlerInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(osoriLoginHandlerInterceptor);

    }


    @Bean
    public FilterRegistrationBean crossScriptingFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new CrossScriptingFilter());
        registration.setOrder(1);
        return registration;
    }

    @Bean
    public FilterRegistrationBean loginFilter() {
        FilterRegistrationBean registration = new FilterRegistrationBean(new UrlBaseLoginFilter());
        registration.setOrder(2);
        return registration;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/**").addResourceLocations("classpath:/public/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
    }

}
