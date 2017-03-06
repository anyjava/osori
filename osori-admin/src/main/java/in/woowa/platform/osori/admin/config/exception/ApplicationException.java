package in.woowa.platform.osori.admin.config.exception;

import java.text.MessageFormat;

/**
 * 어플리케이션에서 사용 할 Exception
 */
public class ApplicationException extends RuntimeException {

    private String code;
    private String msg;
    private String loggingMsg;

    /**
     * Custom Exception
     *
     * @param error 에러코드
     */
    public ApplicationException(Err error) {
        super(String.format("[%s] %s", error.code(), error.message()));

        bindParams(error, error.message());
    }

    /**
     * Custom Exception
     *
     * @param error  에러코드
     * @param errMsg 커스터마이징 할 에러 메시지
     */
    public ApplicationException(Err error, String errMsg) {
        super(String.format("[%s] %s", error.code(), errMsg));

        bindParams(error, errMsg);
    }

    /**
     * Custom Exception
     *
     * @param error      에러코드
     * @param errMsg     커스터마이징 할 에러 메시지
     * @param loggingMsg 별도 로그로 남겨야 할 상세 정보들
     */
    public ApplicationException(Err error, String errMsg, String loggingMsg) {
        super(String.format("[%s] %s", error.code(), errMsg));
        bindParams(error, errMsg);
        this.loggingMsg = "logging msg: " + loggingMsg;
    }

    /**
     * Custom Exception
     *
     * @param error   에러코드
     * @param msgArgs 에러 메시지 출력 시 같이 출력되어야 할 파라미터들 ex) new Object[]{"value1","value2"}
     */
    public ApplicationException(Err error, Object[] msgArgs) {
        super(MessageFormat.format("[{0}] {1}", error.code(), error.message(msgArgs)));

        bindParams(error, error.message(msgArgs));
    }

    /**
     * Custom Exception
     *
     * @param error      에러코드
     * @param msgArgs    에러 메시지 출력 시 같이 출력되어야 할 파라미터들 ex) new Object[]{"value1","value2"}
     * @param loggingMsg 별도 로그로 남겨야 할 상세 정보들
     */
    public ApplicationException(Err error, Object[] msgArgs, String loggingMsg) {
        super(String.format("[%s] %s", new Object[]{error.code(), error.message(msgArgs)}));

        bindParams(error, error.message(msgArgs));
        this.loggingMsg = "logging msg: " + loggingMsg;
    }

    private void bindParams(Err error, String errMsg) {
        this.code = error.code();
        this.msg = errMsg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }

    public String getLoggingMsg() {
        return loggingMsg;
    }
}
