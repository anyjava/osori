package in.woowa.platform.osori.admin.config.exception;

import com.google.common.base.Joiner;

/**
 * Created by seooseok on 2016. 7. 6..
 * 어플리케이션 자체 시스템 에러 및 연관 시스템 간 통신 에러
 */
public enum SystemErr implements Err {




    ERROR_UNKNOWN("S999", "알 수 없는 오류가 발생하였습니다. 업주/업소 개발팀에 문의해주세요. 업무 JIRA: http://jira.woowa.in/secure/RapidBoard.jspa?rapidView=82&projectKey=TWOUPMNG "),
    ERROR_ENCRYPT("S001","암호화에 실패하였습니다. ")
    ;

    private final String code;
    private final String message;

    SystemErr(String code, String message) {
        this.code = code;
        this.message = message;
    }

    @Override
    public String code() {
        return code;
    }

    @Override
    public String message() {
        return message;
    }

    @Override
    public String message(Object... args) {
        return message + " (" + Joiner.on(",").join(args) + ")";
    }
}
