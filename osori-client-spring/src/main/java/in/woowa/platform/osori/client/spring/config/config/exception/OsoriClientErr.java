package in.woowa.platform.osori.client.spring.config.config.exception;

/**
 * Created by HanJaehyun on 2016. 11. 30..
 */
public enum OsoriClientErr {
    NOT_FOUND_ACCOUNT("대상 계정값을 찾을 수 없습니다"),
    INVALID_ACCESS("비정상적인 접근입니다."),
    INVALID_ACCESS_BY_ID("Annotation에 ID값을 설정해주세요."),
    INVALID_AUTHORITY("접근 권한이 없습니다.");

    private String msg;

    OsoriClientErr(String msg){
        this.msg = msg;
    }

    public String getMsg(){
        return msg;
    }
}
