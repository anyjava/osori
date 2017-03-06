package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.data.resource.TreeBranchResource;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.AuthorityNavigationUnit;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.manager.AuthorityDefinitionManager;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import in.woowa.platform.osori.admin.manager.ProjectManager;
import in.woowa.platform.osori.admin.repository.AuthorityNavigationUnitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 9. 30..
 */
@Service
@Transactional
public class AuthorityBundle {

    @Autowired
    private ProjectManager projectManager;
    @Autowired
    private AuthorityDefinitionManager authorityDefinitionManager;
    @Autowired
    private AuthorityNavigationUnitRepository authorityNavigationUnitRepository;
    @Autowired
    private MenuNavigationManager menuNavigationManager;


    public long createBundle(long projectId, String name, List<Long> menuNavigationIdGroup){

        Project project = projectManager.findBy(projectId);
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.createBy(project, name);

        this.addBy(authorityDefinition.getId(), menuNavigationIdGroup);

        return authorityDefinition.getId();
    }

    public List<AuthorityDefinition> getBundles(long projectId) {
        return authorityDefinitionManager.findListBy(projectId);
    }

    public List<MenuNavigationResource> lookInBundle(long authorityDefinitionId){
        List<MenuNavigationResource> menuNavigationResources = new ArrayList<>();
        List<MenuNavigation> currentMenuNavigationGroup = this.hasMenuNavigations(authorityDefinitionId);

        for(MenuNavigation menuNavigation : currentMenuNavigationGroup){
            String fullUrl = menuNavigationManager.getFullUrl(menuNavigation);
            menuNavigationResources.add(MenuNavigationResource.of(menuNavigation, fullUrl));
        }

        return menuNavigationResources;
    }


    public List<TreeBranchResource> lookInBundleForTreeFormat(long authorityDefinitionId){
        List<TreeBranchResource> treeBranchResources = new ArrayList<>();
        List<MenuNavigation> currentMenuNavigationGroup = this.hasMenuNavigations(authorityDefinitionId);

        for(MenuNavigation menuNavigation : currentMenuNavigationGroup){
            String fullUrl = menuNavigationManager.getFullUrl(menuNavigation);

            MenuNavigationResource menuNavigationResource = MenuNavigationResource.of(menuNavigation, fullUrl);
            treeBranchResources.add(TreeBranchResource.of(menuNavigation.getTreeId(), menuNavigation.getParentTreeId(), menuNavigationResource));
        }

        return treeBranchResources;
    }

    public void modifyBundlesNavigation(long projectId, long authorityDefinitionId, long menuNavigationId){

        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findBy(authorityDefinitionId);
        MenuNavigation menuNavigation = menuNavigationManager.findBy(projectId, menuNavigationId);

        if(authorityDefinition == null || menuNavigation == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        authorityDefinition.addBy(menuNavigation);
    }

    protected List<MenuNavigation> hasMenuNavigations(long authorityDefinitionId){
        List<AuthorityNavigationUnit> authorityNavigationUnits = authorityNavigationUnitRepository.findByStatusTrueAndAuthorityDefinitionId(authorityDefinitionId);

        return authorityNavigationUnits
                .stream()
                .map(AuthorityNavigationUnit::getMenuNavigation)
                .collect(Collectors.toList());
    }

    public void addBy(long authorityDefinitionId, List<Long> navigationIdGroup){
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findByLive(authorityDefinitionId);
        long projectId = authorityDefinition.getProject().getId();

        List<MenuNavigation> menuNavigations = menuNavigationManager.findByLive(projectId, navigationIdGroup);

        List<MenuNavigation> targetMenuNavigations = new ArrayList<>();

        for(MenuNavigation menuNavigation : menuNavigations){
            this.putInParentMenuNavigation(targetMenuNavigations, menuNavigation);
        }

        List<MenuNavigation> currentMenuNavigationGroup = authorityDefinition.getMenuNavigations();

        targetMenuNavigations.forEach(menuNavigation -> {
            if(!currentMenuNavigationGroup.contains(menuNavigation))
                authorityDefinition.addBy(menuNavigation);
        });
    }

    public void expireAuthorityNavigation(long projectId, long authorityDefinitionId, List<Long> menuNavigationIdGroup){
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findByLive(authorityDefinitionId);

        if(authorityDefinition.getProject().getId() != projectId)
            throw new ApplicationException(HumanErr.INVALID_INCLUDE);

        List<MenuNavigation> menuNavigations = menuNavigationManager.findByLive(projectId, menuNavigationIdGroup);

        menuNavigations.forEach(menuNavigation -> {
            authorityDefinition.removeBy(menuNavigation);
        });
    }

    public void expireBy(long projectId, long authorityDefinitionId){
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findByLive(authorityDefinitionId);

        if(authorityDefinition.getProject().getId() != projectId)
            throw new ApplicationException(HumanErr.INVALID_INCLUDE);

        authorityDefinition.expire();
    }

    private void putInParentMenuNavigation(List<MenuNavigation> menuNavigations, MenuNavigation menuNavigation){
        menuNavigations.add(menuNavigation);

        MenuNavigation parentMenuNavigation =  menuNavigation.getParentMenuNavigation();

        if(parentMenuNavigation != null && !menuNavigations.contains(parentMenuNavigation)){
            putInParentMenuNavigation(menuNavigations, parentMenuNavigation);
        }
    }
}
