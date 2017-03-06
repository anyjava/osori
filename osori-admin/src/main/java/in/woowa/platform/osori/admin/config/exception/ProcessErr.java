package in.woowa.platform.osori.admin.config.exception;

import java.text.MessageFormat;

/**
 * Created by seooseok on 2016. 7. 12..
 * 어플리케이션의 비즈니스 제어 에러
 */
public enum  ProcessErr implements Err {

    FAIL_IMAGE_CONTROL("P001", "이미지 처리를 실패하였습니다. {0}"),
    FAIL_USER_AUTH("P002", "유저 계정 처리에 실패하였습니다. {0}"),
    ALREADY_EXPIRED("P003", "이미 만료되었습니다. {0}"),
    WRONG_DEVELOP_PROCESS("P004", "해당 요청이 잘못된 작업을 수행하고 있습니다."),
    ;

    private final String code;
    private final String message;

    ProcessErr(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message.replaceAll("\\{.*\\}", "");
    }

    @Override
    public String message(Object... args) {
        return MessageFormat.format(message, args).replaceAll("\\{.*\\}", "");
    }
}
