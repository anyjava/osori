package in.woowa.platform.osori.admin.data.resource;

import lombok.Builder;
import lombok.Getter;

/**
 * Created by seooseok on 2016. 10. 24..
 * 권한 체크 요청 리소스
 */
@Getter
@Builder
public class AccessResource {
    private String target;
    private boolean accessResult;
    private String checkWay;
    private String cause;

    public static AccessResource ofAccept(long targetMenuNavigationId){
        return builder()
                .target(String.valueOf(targetMenuNavigationId))
                .accessResult(true)
                .checkWay("ID")
                .build();
    }

    public static AccessResource ofDenied(long targetMenuNavigationId, String cause){
        return builder()
                .target(String.valueOf(targetMenuNavigationId))
                .accessResult(false)
                .checkWay("ID")
                .cause(cause)
                .build();
    }


    public static AccessResource ofAccept(String targetUrl){
        return builder()
                .target(targetUrl)
                .accessResult(true)
                .checkWay("URI")
                .build();
    }

    public static AccessResource ofDenied(String targetUrl, String cause){
        return builder()
                .target(targetUrl)
                .accessResult(false)
                .checkWay("URI")
                .cause(cause)
                .build();
    }
}
