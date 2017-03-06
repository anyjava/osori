package in.woowa.platform.osori.admin.controller.web.projects;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.data.resource.ProjectResource;
import in.woowa.platform.osori.admin.data.resource.UserResource;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.manager.ProjectManager;
import in.woowa.platform.osori.admin.manager.UserManager;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 9. 20..
 */
@Controller
public class ProjectController {

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private UserManager userManager;

    @GetMapping("/projects")
    @ResponseBody
    public ApiRes findAll(){
        List<Project> projects = projectManager.findAllByLive();

        List<ProjectResource> projectResources = projects
                .stream()
                .map(ProjectResource::of)
                .collect(Collectors.toList());

        return new ApiRes(projectResources);
    }


    @PostMapping("/project")
    @ResponseBody
    public ApiRes create(@Valid @NotEmpty @RequestParam String name, @RequestParam(defaultValue = "") String description){
        long id = projectManager.register(name, description);

        return new ApiRes(id);
    }

    @GetMapping(value = "/project/{id}")
    @ResponseBody
    public ApiRes findOne(@PathVariable long id){
        Project project = projectManager.findBy(id);

        ProjectResource projectResource = ProjectResource.of(project);

        return new ApiRes(projectResource);
    }

    @GetMapping("/project/{id}/users")
    @ResponseBody
    public ApiRes findUsersProject(@PathVariable long id){
        List<UserResource> users = userManager.findAllByJoinedProjectUsers(id);

        return new ApiRes(users);
    }

    @GetMapping("/project/{id}/navigations")
    @ResponseBody
    public ApiRes findNavigationsProject(@PathVariable long id){
        List<MenuNavigationResource> menuNavigationResources = projectManager.findAllByIncludeNavigationsProject(id);

        return new ApiRes(menuNavigationResources);
    }
}
