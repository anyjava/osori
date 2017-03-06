package in.woowa.platform.osori.admin.commons.utils;

import com.google.common.base.Strings;
import in.woowa.platform.osori.admin.commons.annotations.LoginCheck;
import in.woowa.platform.osori.admin.data.OsoriSessionInfo;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.regex.Pattern;

/**
 * Created by seooseok on 2016. 9. 26..
 * request를 이용한 각종 유틸 제공
 */
public class RequestHelper {

    private static final Pattern apiUriPattern = Pattern.compile("/api");
    private static final Pattern jsonPattern = Pattern.compile(MediaType.APPLICATION_JSON_VALUE);

    public static boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String accept = request.getHeader(HttpHeaders.ACCEPT);
        String contentType = request.getHeader(HttpHeaders.CONTENT_TYPE);

        return apiUriPattern.matcher(uri).find()
                || !Strings.isNullOrEmpty(accept) && jsonPattern.matcher(accept).find()
                || !Strings.isNullOrEmpty(contentType) && jsonPattern.matcher(contentType).find();

    }

    public static boolean hasResponseBodyAnnotation(HandlerMethod handlerMethod){
        ResponseBody responseBody = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), ResponseBody.class);

        if(responseBody == null)
            responseBody = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), ResponseBody.class);

        return responseBody != null;

    }

    public static boolean hasLoginCheckAnnotation(HandlerMethod handlerMethod){
        LoginCheck loginCheck = AnnotationUtils.findAnnotation(handlerMethod.getBeanType(), LoginCheck.class);

        if(loginCheck == null || !loginCheck.enable())
            loginCheck = AnnotationUtils.findAnnotation(handlerMethod.getMethod(), LoginCheck.class);

        return !(loginCheck == null || !loginCheck.enable());

    }

    public static void addSessionBy(HttpSession httpSession, OsoriSessionInfo osoriSessionInfo){
        httpSession.setAttribute(AttrKey.adminId.name(), osoriSessionInfo.getAdminId());
        httpSession.setAttribute(AttrKey.adminEmail.name(), osoriSessionInfo.getAdminEmail());
        httpSession.setAttribute(AttrKey.adminName.name(), osoriSessionInfo.getAdminName());
    }

    public static void addSessionBy(HttpServletRequest request, OsoriSessionInfo osoriSessionInfo){
        RequestHelper.addSessionBy(request.getSession(), osoriSessionInfo);
    }

    public static OsoriSessionInfo getSessionByAdmin(HttpSession httpSession) throws IllegalArgumentException{

        Object adminId = httpSession.getAttribute(AttrKey.adminId.name());
        Object adminEmail = httpSession.getAttribute(AttrKey.adminEmail.name());
        Object adminName = httpSession.getAttribute(AttrKey.adminName.name());

        if(adminId == null || adminEmail == null || adminName == null)
            throw new IllegalArgumentException("admin login session is null");

        return OsoriSessionInfo.builder()
                .adminId(((Long) adminId))
                .adminEmail((String) adminEmail)
                .adminName((String) adminName)
                .build();
    }

    public static OsoriSessionInfo getSessionByAdmin(HttpServletRequest request){
        return RequestHelper.getSessionByAdmin(request.getSession());
    }


    private enum AttrKey {
        adminId,adminEmail,adminName
    }
}
