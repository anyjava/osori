package in.woowa.platform.osori.admin.config.filter;


import com.google.common.base.Strings;
import org.apache.commons.lang3.ArrayUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 * Request 파라미터 관련 처리
 */
class RequestWrapper extends HttpServletRequestWrapper {

    private static final String XSS_FORMAT = "((<|<[/])script>)|((<|<[/])javascript>)|((<|<[/])vbscript>)|((<|<[/])alert>)|((<|<[/])confirm>)";

    public RequestWrapper(HttpServletRequest servletRequest) {
        super(servletRequest);
    }

    @Override
    public String[] getParameterValues(String parameter) {

        String[] values = super.getParameterValues(parameter);


        if (ArrayUtils.isEmpty(values)) {
            return null;
        }

        if ("pw".equals(parameter))
            return values;

        int count = values.length;
        String[] encodedValues = new String[count];

        for (int i = 0; i < count; i++) {
            encodedValues[i] = this.cleanXSS(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        if (Strings.isNullOrEmpty(value))
            return value;

        //password는 cleanXss를 처리하지 않는다.
        if ("pw".equals(parameter))
            return value;

        return this.cleanXSS(value);
    }

    /**
     * XSS 코드를 싹 날려버림 (web에서는 escape 처리 안함)
     *
     * @param param request 파라미터
     * @return XSS 코드 날린 param
     */
    private String cleanXSS(String param) {
        return param.replaceAll(XSS_FORMAT, "");
    }

}
