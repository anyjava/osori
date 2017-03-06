package in.woowa.platform.osori.admin.data;

import in.woowa.platform.osori.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by seooseok on 2016. 10. 12..
 */
@Builder
@Getter
public class OsoriSessionInfo {

    private long adminId;
    private String adminEmail;
    private String adminName;

    public static OsoriSessionInfo of(Admin admin){
        return builder()
                .adminId(admin.getId())
                .adminEmail(admin.getEmail())
                .adminName(admin.getName())
                .build();
    }
}
