package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.commons.constant.DateFormat;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * Created by seooseok on 2016. 10. 10..
 */
@Builder
@Data
public class AuthorityGrantResource {

    private long id;
    private String name;
    private String regDate;

    private long projectId;
    private String projectName;


    public static AuthorityGrantResource of(AuthorityDefinition authorityDefinition, Date regDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());

        return builder()
                .id(authorityDefinition.getId())
                .name(authorityDefinition.getName())
                .regDate(formatter.format(regDate.toInstant()))
                .projectId(authorityDefinition.getProject().getId())
                .projectName(authorityDefinition.getProject().getName())
                .build();
    }
}
