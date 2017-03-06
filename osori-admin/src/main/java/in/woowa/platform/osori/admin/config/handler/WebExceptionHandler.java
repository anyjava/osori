package in.woowa.platform.osori.admin.config.handler;

import com.google.common.base.Strings;
import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.ProcessErr;
import in.woowa.platform.osori.admin.config.exception.SystemErr;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Url과 관련된 Exception
 */
@Slf4j
@ControllerAdvice(annotations = {Controller.class})
public class WebExceptionHandler {

    @ExceptionHandler({RuntimeException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleRunTimeException(RuntimeException e, Model model, HttpServletRequest request) {

        if(RequestHelper.isApiRequest(request)){
            model.addAttribute("format","json");
        }

        model.addAttribute("code", SystemErr.ERROR_UNKNOWN.code());
        model.addAttribute("message", ExceptionUtils.getRootCauseMessage(e));
        model.addAttribute("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        model.addAttribute("loggingMsg", ExceptionUtils.getStackTrace(e));

        return "error";
    }

    @ExceptionHandler({ApplicationException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleStoreException(ApplicationException e, Model model, HttpServletRequest request) {

        if(RequestHelper.isApiRequest(request)){
            model.addAttribute("format","json");
        }

        model.addAttribute("code", e.getCode());
        model.addAttribute("message", ExceptionUtils.getMessage(e));
        model.addAttribute("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        if(Strings.isNullOrEmpty(e.getLoggingMsg()))
            log.error(e.getLoggingMsg());
        return "error";
    }

    @ExceptionHandler({IllegalStateException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleIllegalStateException(IllegalStateException e, Model model, HttpServletRequest request){
        if(RequestHelper.isApiRequest(request)){
            model.addAttribute("format","json");
        }

        model.addAttribute("code", ProcessErr.WRONG_DEVELOP_PROCESS);
        model.addAttribute("message", ExceptionUtils.getMessage(e));
        model.addAttribute("timestamp", LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());

        return "error";
    }



}
