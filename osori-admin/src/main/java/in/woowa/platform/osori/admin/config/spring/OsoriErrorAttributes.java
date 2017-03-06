package in.woowa.platform.osori.admin.config.spring;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.RequestAttributes;

import javax.servlet.ServletException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by seooseok on 2016. 9. 29..
 * API servlet error spec 정의
 */
@Configuration
public class OsoriErrorAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace) {
        Map<String, Object> errorAttributes = new LinkedHashMap<>();
        errorAttributes.put("timestamp", new Date());

        this.addCode(errorAttributes, requestAttributes);
        this.addErrorDetails(errorAttributes, requestAttributes);

        return errorAttributes;
    }

    private void addCode(Map<String, Object> errorAttributes, RequestAttributes requestAttributes) {
        Integer status = getAttribute(requestAttributes,"javax.servlet.error.status_code");

        if (status == null) {
            errorAttributes.put("code", "S998");
            return;
        }
        errorAttributes.put("code", status);
    }

    private void addErrorDetails(Map<String, Object> errorAttributes,RequestAttributes requestAttributes) {

        Throwable error = getError(requestAttributes);
        if (error != null) {
            while (error instanceof ServletException && error.getCause() != null) {
                error = (error).getCause();
            }
            errorAttributes.put("exception", error.getClass().getName());
            addErrorMessage(errorAttributes, error);
        }
        Object message = getAttribute(requestAttributes, "javax.servlet.error.message");
        if ((!StringUtils.isEmpty(message) || errorAttributes.get("message") == null)
                && !(error instanceof BindingResult)) {
            errorAttributes.put("message",
                    StringUtils.isEmpty(message) ? "No message available" : message);
        }
    }

    private void addErrorMessage(Map<String, Object> errorAttributes, Throwable error) {
        BindingResult result = extractBindingResult(error);
        if (result == null) {
            errorAttributes.put("message", error.getMessage());
            return;
        }
        if (result.getErrorCount() > 0) {
            errorAttributes.put("message",
                    "Validation failed for object='" + result.getObjectName()
                            + "'. Error count: " + result.getErrorCount());
        }
        else {
            errorAttributes.put("message", "No errors");
        }
    }


    private BindingResult extractBindingResult(Throwable error) {
        if (error instanceof BindingResult) {
            return (BindingResult) error;
        }
        if (error instanceof MethodArgumentNotValidException) {
            return ((MethodArgumentNotValidException) error).getBindingResult();
        }
        return null;
    }

    private <T> T getAttribute(RequestAttributes requestAttributes, String name) {
        return (T) requestAttributes.getAttribute(name, RequestAttributes.SCOPE_REQUEST);
    }

}
