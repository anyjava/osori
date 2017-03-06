package in.woowa.platform.osori.admin.config.filter;

import in.woowa.platform.osori.admin.commons.utils.RequestHelper;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by seooseok on 2016. 10. 31..
 */
public class UrlBaseLoginFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {

        HttpServletRequest servletRequest = (HttpServletRequest) request;

        if(servletRequest.getRequestURI().startsWith("/view")){
            try{
                RequestHelper.getSessionByAdmin(servletRequest);
            } catch (IllegalArgumentException e){
                ((HttpServletResponse) response).sendRedirect("/login.html");
            }
        }

        chain.doFilter(request,response);
    }

    @Override
    public void destroy() {

    }
}
