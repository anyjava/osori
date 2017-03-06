package in.woowa.platform.osori.admin.config.handler;

import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by seooseok on 2016. 8. 19..
 * 로그인 인터프리터
 */
@Configuration
public class OsoriLoginHandlerInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        //Handler가 HandlerMethod가 아닌 경우는 Login을 체크 할 수 없기 때문에 true를 반환한다.
        if(!(handler instanceof HandlerMethod))
            return true;

        if(!RequestHelper.hasLoginCheckAnnotation((HandlerMethod) handler))
            return true;

        try{
            RequestHelper.getSessionByAdmin(request);
        } catch (IllegalArgumentException e){
            response.sendRedirect("/login.html");
            return false;
        }

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
