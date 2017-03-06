package in.woowa.platform.osori.admin.commons;


import in.woowa.platform.osori.admin.config.exception.Err;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * Created by seooseok on 2016. 6. 30..
 * 공통 API 응답 클래스
 */
@SuppressWarnings("unused")
public class ApiRes<T> {

    private static final String okCode = "0000";
    private static final String okMessage = "ok";

    private final long timestamp;   //응답 시간
    private final String code;        //상태
    private final String message;     //메시지
    private final T result;

    public ApiRes(T t) {
        this.timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.code = okCode;
        this.message = okMessage;
        this.result = t;

    }

    private ApiRes(String code, String message) {
        this.timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.code = code;
        this.message = message;
        this.result = null;

    }

    private ApiRes(Err err) {
        this.timestamp = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
        this.code = err.code();
        this.message = err.message();
        this.result = null;
    }

    public static ApiRes success() {
        return new ApiRes(okCode, okMessage);
    }

    public static ApiRes fail(Err err) {
        return new ApiRes(err);
    }

    public static ApiRes fail(String code, String message) {
        return new ApiRes(code, message);
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public T getResult() {
        return result;
    }

    public long getTimestamp() {
        return timestamp;
    }
}
