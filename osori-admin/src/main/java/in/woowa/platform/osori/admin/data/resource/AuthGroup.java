package in.woowa.platform.osori.admin.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import lombok.Builder;
import lombok.Getter;

/**
 * Created by HanJaehyun on 2016. 10. 11..
 */
@Builder
@Getter
public class AuthGroup {
    private String name;
    private long projectId;

    @JsonProperty("authId")
    private long id;

    public static AuthGroup of(AuthorityDefinition authorityDefinition){
        return builder()
                .name(authorityDefinition.getName())
                .projectId(authorityDefinition.getProject().getId())
                .id(authorityDefinition.getId())
                .build();
    }
}
