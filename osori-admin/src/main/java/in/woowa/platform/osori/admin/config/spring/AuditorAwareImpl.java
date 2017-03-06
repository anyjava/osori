package in.woowa.platform.osori.admin.config.spring;


import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import in.woowa.platform.osori.admin.data.OsoriSessionInfo;
import org.springframework.data.domain.AuditorAware;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;


/**
 * Created by seooseok on 2016. 7. 5..
 * 승인자 Auditor
 */
public class AuditorAwareImpl implements AuditorAware<String> {

    @Override
    public String getCurrentAuditor() {

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        if(request.getRequestURI().startsWith("/api"))
            return "api@osori.com";

        OsoriSessionInfo osoriSessionInfo = RequestHelper.getSessionByAdmin(request);

        return osoriSessionInfo.getAdminEmail();
    }
}
