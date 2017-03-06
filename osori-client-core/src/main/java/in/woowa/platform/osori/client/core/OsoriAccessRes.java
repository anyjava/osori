package in.woowa.platform.osori.client.core;

/**
 * Created by HanJaehyun on 2016. 11. 10..
 */
public class OsoriAccessRes {
    private String target;
    private boolean accessResult;
    private String checkWay;
    private String cause;

    public OsoriAccessRes(){}

    public String getTarget() {
        return target;
    }

    public boolean isAccessResult() {
        return accessResult;
    }

    public String getCheckWay() {
        return checkWay;
    }

    public String getCause() {
        return cause;
    }
}
