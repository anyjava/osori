package in.woowa.platform.osori.admin.config.handler;

import com.google.common.base.Strings;
import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.ProcessErr;
import in.woowa.platform.osori.admin.config.exception.SystemErr;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.text.MessageFormat;
import java.util.Locale;

/**
 * 공통 에러를 처리한다.
 */
@Slf4j
@ControllerAdvice(annotations = {RestController.class})
public class ApiExceptionHandler {

    private static final String GIT_WIKI = "https://github.com/woowabros/osori/wiki";

    @Autowired
    MessageSource messageSource;

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiRes handleRunTimeException(RuntimeException e) {
        log.error(e.getMessage(), e);
        return ApiRes.fail(SystemErr.ERROR_UNKNOWN);
    }

    @ExceptionHandler({ApplicationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiRes handleApplicationException(ApplicationException e) {
        if(Strings.isNullOrEmpty(e.getLoggingMsg()))
            log.error(e.getLoggingMsg());

        return ApiRes.fail(e.getCode(), e.getMsg());
    }

    @ExceptionHandler(IllegalStateException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ResponseBody
    public ApiRes illegalStateException(IllegalStateException e){
        return ApiRes.fail(ProcessErr.WRONG_DEVELOP_PROCESS.code(), e.getMessage() + ".");
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleConstraintViolationException(ConstraintViolationException e) {
        StringBuilder sb = new StringBuilder();

        for (ConstraintViolation<?> violation : e.getConstraintViolations()) {
            sb.append(violation.getPropertyPath()).append("는(은) ").append(violation.getMessage()).append(" ");
        }
        return ApiRes.fail(HumanErr.INVALID_ARGS.code(), sb.toString());
    }

    /**
     * Valid를 하는 객체에 대한 Exception
     *
     * @return ApiRes
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        BindingResult bindingResult = e.getBindingResult();
        String errMsg = this.createErrMsg(bindingResult);

        return ApiRes.fail(HumanErr.INVALID_ARGS.code(), errMsg);
    }

    /**
     * API로 요청이 온 파라미터를 Controller 파라미터로 bind하다가 실패 한 경우
     */
    @ExceptionHandler(ServletRequestBindingException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleRequestBindException(ServletRequestBindingException e, HttpServletRequest request) {
        return ApiRes.fail(HumanErr.FAIL_BIND.code(), HumanErr.FAIL_BIND.message(request.getRequestURI(), GIT_WIKI));
    }

    /**
     * 필수 파라미터로 지정한 파라미터가 없는 경우
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {
        return ApiRes.fail(HumanErr.MISSING_PARAM.code(), HumanErr.MISSING_PARAM.message(e.getParameterName()));
    }

    /**
     * API로 요청한 파라미터들의 타입이 잘못 된 경우
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String mismatchMsg = "(" + e.getName() + "의 요청타입은 " + e.getRequiredType() + "입니다)";
        return ApiRes.fail(HumanErr.INVALID_ARGS.code(), HumanErr.INVALID_ARGS.message(mismatchMsg));
    }

    private String createErrMsg(BindingResult bindingResult) {
        StringBuilder sb = new StringBuilder();

        //바인딩이 여러개 한번에 실패하는 경우 메시지를 모두 출력하지 않고 필드명만 나열해서 해당 필드가 잘못되었다고 알려준다.
        if (bindingResult.getFieldErrors().size() > 1) {
            bindingResult.getAllErrors().stream().filter(object -> object instanceof FieldError).forEach(object -> {
                FieldError fieldError = (FieldError) object;
                sb.append(fieldError.getField()).append(" ");
            });
            sb.append("필드들의 입력이 잘못되었습니다");
        } else {
            FieldError fieldError = bindingResult.getFieldError();
            Locale currentLocale = LocaleContextHolder.getLocale();

            String errMsg = messageSource.getMessage(fieldError, currentLocale);

            if (errMsg.equals(fieldError.getDefaultMessage()))
                errMsg = MessageFormat.format("{0} 필드의 입력이 잘못되었습니다", fieldError.getField());

            sb.append(errMsg);
        }

        return sb.toString();
    }

    /**
     * API 정의한 Method와 요청을 준 Method가 다른 경우
     *
     * @param e       HttpRequestMethodNotSupportedException
     * @param request HttpServletRequest
     * @return ApiRes
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ResponseBody
    public ApiRes handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e, HttpServletRequest request) {
        return ApiRes.fail(String.valueOf(HttpStatus.METHOD_NOT_ALLOWED.value()), "해당 API는 " + request.getMethod() + "를 지원하지 않습니다.");
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ResponseBody
    public ApiRes handleIllegalArgumentException(IllegalArgumentException e) {
        return ApiRes.fail(HumanErr.INVALID_ARGS.code(), HumanErr.INVALID_ARGS.message(e.getMessage()));
    }

}
