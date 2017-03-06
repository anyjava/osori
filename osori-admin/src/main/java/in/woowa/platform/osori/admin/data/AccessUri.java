package in.woowa.platform.osori.admin.data;

import lombok.Builder;
import lombok.Getter;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by seooseok on 2016. 10. 27..
 * 권한 체크 할 URL정보
 */
@Builder
@Getter
public class AccessUri {
    private String uri;
    private RequestMethod requestMethod;

    public static AccessUri of(String uri, RequestMethod requestMethod){
        return builder()
                .uri(uri)
                .requestMethod(requestMethod)
                .build();
    }
}
