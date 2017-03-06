package in.woowa.platform.osori.admin.config.filter;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * XSS 방지 필터
 */
public class CrossScriptingFilter implements Filter {

    private FilterConfig filterConfig;

    /**
     * 필터 초기화
     *
     * @param filterConfig filterConfig
     * @throws ServletException
     */
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }


    /**
     * 필터 체이닝
     *
     * @param request  ServletRequest
     * @param response ServletResponse
     * @param chain    FilterChain
     * @throws IOException
     * @throws ServletException
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        chain.doFilter(new RequestWrapper((HttpServletRequest) request), response);
    }


    /**
     * 필터 완료 처리
     */
    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
