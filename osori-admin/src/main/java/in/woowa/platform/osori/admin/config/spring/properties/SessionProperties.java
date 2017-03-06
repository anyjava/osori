package in.woowa.platform.osori.admin.config.spring.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Created by seooseok on 2016. 7. 12..
 * 세션 저장용 redis 접속 정보
 */
@Data
@Component
@ConfigurationProperties(prefix = SessionProperties.PREFIX)
public class SessionProperties {
    public static final String PREFIX = "session.redis";

    private String url;
    private int port;

}
