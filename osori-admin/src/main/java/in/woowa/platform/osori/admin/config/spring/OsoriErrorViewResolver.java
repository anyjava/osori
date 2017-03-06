package in.woowa.platform.osori.admin.config.spring;

import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import org.springframework.boot.autoconfigure.web.ErrorViewResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Map;

/**
 * Created by seooseok on 2016. 9. 19..
 * View Error 처리
 */
@Configuration
public class OsoriErrorViewResolver implements ErrorViewResolver {

    @Override
    public ModelAndView resolveErrorView(HttpServletRequest request, HttpStatus status, Map<String, Object> model) {
        ModelAndView mav = new ModelAndView("error");

        if(RequestHelper.isApiRequest(request))
            mav.addObject("format", "json");

        mav.addObject("code", status.value());
        mav.addObject("message",status.getReasonPhrase());
        mav.addObject("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());


        return mav;
    }

}
