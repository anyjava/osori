package in.woowa.platform.osori.admin.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonView;
import in.woowa.platform.osori.admin.commons.constant.DateFormat;
import in.woowa.platform.osori.admin.data.JView;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.entity.UserAuthorityGrant;
import in.woowa.platform.osori.admin.entity.UserPersonalGrant;
import lombok.Builder;
import lombok.Data;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 6..
 */
@Builder
@Data
public class UserResource {

    private long id;
    private String email;
    private String name;
    private String department;
    private boolean accessPrivacyInformation;
    private String regDate;
    private String status;

    @JsonView(JView.Detail.class)
    @JsonProperty("projects")
    private List<ProjectResource> projectResources;

    @JsonView(JView.Detail.class)
    @JsonProperty("authorityDefinitions")
    private List<AuthorityGrantResource> authorityGrantResources;

    @JsonView(JView.Detail.class)
    @JsonProperty("menuNavigations")
    private List<PersonalGrantResource> personalGrantResources;


    public static UserResource of(User user){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());
        return builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .accessPrivacyInformation(user.isPrivacy())
                .regDate(formatter.format(user.getCreated().toInstant()))
                .status(user.getStatus().name())
                .build();
    }

    public static UserResource ofDetail(User user, List<Project> projects, List<UserAuthorityGrant> userAuthorityGrants, List<UserPersonalGrant> userPersonalGrants){
        List<ProjectResource> projectResources = new ArrayList<>();
        List<AuthorityGrantResource> authorityGrantResources = new ArrayList<>();
        List<PersonalGrantResource> personalGrantResources = new ArrayList<>();

        projects.forEach(project -> {
            projectResources.add(ProjectResource.of(project));
        });

        userAuthorityGrants.forEach(userAuthorityGrant -> {
            authorityGrantResources.add(AuthorityGrantResource.of(userAuthorityGrant.getAuthorityDefinition(), userAuthorityGrant.getCreated()));
        });

        userPersonalGrants.forEach(userPersonalGrant -> {
            personalGrantResources.add(PersonalGrantResource.of(userPersonalGrant.getMenuNavigation(), userPersonalGrant.getCreated()));
        });

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DateFormat.localDate).withZone(ZoneId.systemDefault());

        return builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .department(user.getDepartment())
                .accessPrivacyInformation(user.isPrivacy())
                .regDate(formatter.format(user.getCreated().toInstant()))
                .status(user.getStatus().name())
                .projectResources(projectResources)
                .authorityGrantResources(authorityGrantResources)
                .personalGrantResources(personalGrantResources)
                .build();
    }


}
