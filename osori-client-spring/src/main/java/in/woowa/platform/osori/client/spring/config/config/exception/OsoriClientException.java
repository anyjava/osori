package in.woowa.platform.osori.client.spring.config.config.exception;

/**
 * Created by HanJaehyun on 2016. 11. 15..
 */
public class OsoriClientException extends RuntimeException{

    public OsoriClientException(OsoriClientErr err){
        super(String.format("%s", err.getMsg()));
    }
}
