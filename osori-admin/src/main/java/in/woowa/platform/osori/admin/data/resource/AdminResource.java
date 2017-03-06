package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.entity.Admin;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by seooseok on 2016. 10. 12..
 */
@Builder
@Getter
public class AdminResource {

    private long id;
    private String email;
    private String name;
    private String description;
    private String img;

    public static AdminResource of(Admin admin){
        return builder()
                .id(admin.getId())
                .email(admin.getEmail())
                .name(admin.getName())
                .description(admin.getDescription())
                .img(admin.getImg())
                .build();
    }
}
