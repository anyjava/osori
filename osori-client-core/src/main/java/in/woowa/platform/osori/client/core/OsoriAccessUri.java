package in.woowa.platform.osori.client.core;

import java.net.URI;

/**
 * Created by seooseok on 2016. 10. 27..
 */
public class OsoriAccessUri {
    private String uri;
    private String methodType;

    public OsoriAccessUri(URI uri, String methodType) {
        this.uri = uri.toString();
        this.methodType = methodType;
    }

    public String getUri() {
        return uri;
    }


    public String getMethodType() {
        return methodType;
    }
}
