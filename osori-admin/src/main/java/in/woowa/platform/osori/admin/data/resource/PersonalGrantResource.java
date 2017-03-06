package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.commons.constant.DateFormat;
import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
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
public class PersonalGrantResource {

    private Long id;
    private OsoriConstant.NavigationType type;
    private String name;
    private String uriBlock;
    private String regDate;
    private String fullUrl;

    private long projectId;
    private String projectName;

    public static PersonalGrantResource of(MenuNavigation menuNavigation, Date regDate){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());

        return builder()
                .id(menuNavigation.getId())
                .type(menuNavigation.getType())
                .name(menuNavigation.getName())
                .uriBlock(menuNavigation.getUriBlock())
                .regDate(formatter.format(regDate.toInstant()))
                .projectId(menuNavigation.getProject().getId())
                .projectName(menuNavigation.getProject().getName())
                .build();
    }

    public static PersonalGrantResource of(MenuNavigation menuNavigation, Date regDate, String fullUrl){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());

        return builder()
                .id(menuNavigation.getId())
                .type(menuNavigation.getType())
                .name(menuNavigation.getName())
                .uriBlock(menuNavigation.getUriBlock())
                .fullUrl(fullUrl)
                .regDate(formatter.format(regDate.toInstant()))
                .projectId(menuNavigation.getProject().getId())
                .projectName(menuNavigation.getProject().getName())
                .build();
    }
}
