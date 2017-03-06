package in.woowa.platform.osori.client.core;

/**
 * Created by seooseok on 2016. 9. 29..
 */
public class OsoriRes {

    private long timestamp;   //응답 시간
    private String code;        //상태
    private String message;     //메시지
    private Object result;

    public long getTimestamp() {
        return timestamp;
    }

    public String getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public Object getResult() {
        return result;
    }
}
