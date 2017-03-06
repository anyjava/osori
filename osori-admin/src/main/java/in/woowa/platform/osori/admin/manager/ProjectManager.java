package in.woowa.platform.osori.admin.manager;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant.NavigationType;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Service
@Transactional
public class ProjectManager {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private MenuNavigationManager menuNavigationManager;

    public long register(String name, String description){
        Project project = Project.of(name,description);

        MenuNavigation menuNavigation = new MenuNavigation(
                project,
                "1",
                "#",
                name,
                "/",
                NavigationType.category,
                GET
        );
        project.addBy(menuNavigation);

        return projectRepository.save(project).getId();
    }

    public List<Project> findAllByLive() {
        return this.findAll()
                .stream()
                .filter(project -> project.isStatus())
                .collect(Collectors.toList());
    }

    protected List<Project> findAll(){
        return projectRepository.findAll();
    }

    public Project findBy(long id){
        Optional<Project> project = Optional.ofNullable(projectRepository.findOne(id));

        if(!project.isPresent())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        return project.get();
    }

    public Project findByLive(long id){
        Project project = this.findBy(id);

        if(!project.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE);

        return project;
    }

    public Project findByLiveProjectOfApiKey(String apiKey){
        Project project = projectRepository.findByApiKey(apiKey);
        if(project == null)
            throw new ApplicationException(HumanErr.INVALID_APIKEY);

        if(!project.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE);

        return project;
    }

    public List<MenuNavigationResource> findAllByIncludeNavigationsProject(long id){
        List<MenuNavigationResource> menuNavigationResources = new ArrayList<>();

        Project project = this.findByLive(id);

        project.getMenuNavigations().forEach(menuNavigation -> {
            menuNavigationResources.add(MenuNavigationResource.of(menuNavigation, menuNavigationManager.getFullUrl(menuNavigation)));
        });

        return menuNavigationResources;
    }


}
