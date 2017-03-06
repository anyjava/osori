package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.commons.constant.DateFormat;
import in.woowa.platform.osori.admin.entity.Project;
import lombok.Builder;
import lombok.Getter;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;


/**
 * Created by seooseok on 2016. 9. 26..
 */
@Builder
@Getter
public class ProjectResource {

    private long id;
    private String name;
    private String description;
    private String apiKey;
    private String created;
    private boolean status;

    public static ProjectResource of(Project project){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());
        return builder()
                .id(project.getId())
                .name(project.getName())
                .description(project.getDescription())
                .apiKey(project.getApiKey())
                .created(formatter.format(project.getCreated().toInstant()))
                .status(project.isStatus())
                .build();
    }
}
