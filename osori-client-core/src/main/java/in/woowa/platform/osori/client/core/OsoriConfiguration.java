package in.woowa.platform.osori.client.core;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

import java.net.URL;

/**
 * Created by seooseok on 2016. 9. 5..
 * 권한 관리 시스템 설정
 */
public class OsoriConfiguration {

    private final URL host;
    private final String apiKey;
    private final PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
    private final RequestConfig requestConfig;

    public static class Builder {
        private final URL host;
        private final String apiKey;
        private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;
        private RequestConfig requestConfig;


        public Builder(URL host, String apiKey){
            this.host = host;
            this.apiKey = apiKey;

            PoolingHttpClientConnectionManager manager = new PoolingHttpClientConnectionManager();
            manager.setMaxTotal(200);
            this.poolingHttpClientConnectionManager = manager;

            this.requestConfig = RequestConfig.custom()
                    .setConnectionRequestTimeout(1000)
                    .setConnectTimeout(2000)
                    .setSocketTimeout(3000)
                    .build();
        }

        public Builder connection(PoolingHttpClientConnectionManager poolingHttpClientConnectionManager){
            this.poolingHttpClientConnectionManager = poolingHttpClientConnectionManager;
            return this;
        }

        public Builder requestConfig(RequestConfig requestConfig){
            this.requestConfig = requestConfig;
            return this;
        }

        public OsoriConfiguration build() {
            return new OsoriConfiguration(this);
        }
    }

    private OsoriConfiguration(Builder builder){
        host = builder.host;
        apiKey = builder.apiKey;
        poolingHttpClientConnectionManager = builder.poolingHttpClientConnectionManager;
        requestConfig = builder.requestConfig;
    }

    public String getHost() {
        return host.toString();
    }

    public String getApiKey() {
        return apiKey;
    }

    public PoolingHttpClientConnectionManager getPoolingHttpClientConnectionManager() {
        return poolingHttpClientConnectionManager;
    }

    public RequestConfig getRequestConfig() {
        return requestConfig;
    }
}
