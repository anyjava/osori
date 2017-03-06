package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.commons.constant.DateFormat;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

/**
 * Created by seooseok on 2016. 10. 7..
 */
@Builder
@Data
public class AuthorityDefinitionResource {

    private long id;
    private String name;
    private String regDate;

    public static AuthorityDefinitionResource of(AuthorityDefinition authorityDefinition){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());
        return builder()
                .id(authorityDefinition.getId())
                .name(authorityDefinition.getName())
                .regDate(formatter.format(authorityDefinition.getCreated().toInstant()))
                .build();
    }

}
