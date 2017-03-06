package in.woowa.platform.osori.client.spring.config.interceptor;

import in.woowa.platform.osori.client.core.OsoriAccessRes;
import in.woowa.platform.osori.client.core.OsoriAuthById;
import in.woowa.platform.osori.client.core.OsoriClient;
import in.woowa.platform.osori.client.core.common.constants.OsoriConstant;
import in.woowa.platform.osori.client.spring.config.config.exception.OsoriClientErr;
import in.woowa.platform.osori.client.spring.config.config.exception.OsoriClientException;
import org.apache.http.HttpException;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.env.Environment;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.GeneralSecurityException;
import java.util.Arrays;
import java.util.List;

/**
 * Created by HanJaehyun on 2016. 11. 10..
 */
public class OsoriIdBaseAuthHandlerInterceptor implements HandlerInterceptor{

    private String host;
    private String apiKey;
    private String emailAttributeName;
    private String email;

    private void validate(Environment environment) throws NoSuchFieldException {
        for(String prop : OsoriConstant.CONNECTION_PROPERTIES){
            if(!environment.containsProperty(prop)){
                throw new NoSuchFieldException(prop);
            }
        }
    }

    public OsoriIdBaseAuthHandlerInterceptor(Environment environment) throws NoSuchFieldException {
        validate(environment);

        this.host = environment.getProperty("osori.host");
        this.apiKey = environment.getProperty("osori.apiKey");
        this.emailAttributeName = environment.getProperty("osori.emailAttributeName");
    }

    public OsoriIdBaseAuthHandlerInterceptor(String host, String apiKey, String emailAttributeName){
        this.host = host;
        this.apiKey = apiKey;
        this.emailAttributeName = emailAttributeName;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws MalformedURLException, ServletException {
        HttpSession session = request.getSession();

        if(session == null) {
            Cookie[] cookies = request.getCookies();
            for(Cookie cookie : cookies) {
                if(cookie.getName().equals(this.emailAttributeName)) {
                    email = cookie.getValue();
                    break;
                }
            }

        }else if(session.getAttribute(this.emailAttributeName) != null) {
            email = session.getAttribute(this.emailAttributeName).toString();
        }

        if(email == null)
            throw new OsoriClientException(OsoriClientErr.NOT_FOUND_ACCOUNT);

        OsoriAuthById authById = AnnotationUtils.findAnnotation(((HandlerMethod)handler).getMethod(), OsoriAuthById.class);
        int id = authById.id();

        if(id == 0)
            throw new OsoriClientException(OsoriClientErr.INVALID_ACCESS_BY_ID);

        OsoriClient osoriClient = new OsoriClient(host, apiKey);
        List<OsoriAccessRes> result = null;
        try {
            result = osoriClient.authorizationCheckById(email, Arrays.asList(id));
        } catch (GeneralSecurityException | IOException | HttpException e) {
            throw new ServletException(e.getMessage());
        }

        if(result.isEmpty())
            throw new OsoriClientException(OsoriClientErr.INVALID_ACCESS);


        if(!result.get(0).isAccessResult())
            throw new OsoriClientException(OsoriClientErr.INVALID_AUTHORITY);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
