package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.data.resource.BranchRequest;
import in.woowa.platform.osori.admin.data.resource.TreeBranchResource;
import in.woowa.platform.osori.admin.entity.AuthorityNavigationUnit;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import in.woowa.platform.osori.admin.manager.ProjectManager;
import in.woowa.platform.osori.admin.repository.AuthorityNavigationUnitRepository;
import in.woowa.platform.osori.admin.repository.MenuNavigationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by HanJaehyun on 2016. 9. 22..
 */
@Service
@Transactional
public class NavigationTree {

    @Autowired
    private MenuNavigationRepository menuNavigationRepository;
    @Autowired
    private AuthorityNavigationUnitRepository authorityNavigationUnitRepository;
    @Autowired
    private MenuNavigationManager menuNavigationManager;

    @Autowired
    private ProjectManager projectManager;

    public long createBranch(long projectId, BranchRequest branchRequest){
        Project project = projectManager.findByLive(projectId);

        MenuNavigation parentMenuNavigation = menuNavigationRepository.findByProjectIdAndTreeId(projectId, branchRequest.getParentTreeId());

        MenuNavigation menuNavigation = new MenuNavigation(
            project,
            branchRequest.getTreeId(),
            branchRequest.getParentTreeId(),
            branchRequest.getName(),
            branchRequest.getUriBlock(),
            branchRequest.getType(),
            branchRequest.getMethodType()
        );

        menuNavigation.setBy(parentMenuNavigation);

        return this.menuNavigationRepository.save(menuNavigation).getId();
    }


    public List<TreeBranchResource> getTreeBranches(long projectId){
        List<TreeBranchResource> treeBranchResources = new ArrayList<>();

        Project project = projectManager.findByLive(projectId);
        List<MenuNavigation> menuNavigationGroup = project.getMenuNavigations();

        if(menuNavigationGroup.isEmpty())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        for(MenuNavigation menuNavigation : menuNavigationGroup){
            treeBranchResources.add(this.getTreeBranch(menuNavigation));
        }

        return treeBranchResources;
    }


    public TreeBranchResource getTreeBranch(long projectId, long menuNavigationId){
        MenuNavigation navigation = menuNavigationManager.findBy(projectId, menuNavigationId);
        return this.getTreeBranch(navigation);
    }


    private TreeBranchResource getTreeBranch(MenuNavigation menuNavigation){
        String fullUrl = menuNavigationManager.getFullUrl(menuNavigation);

        MenuNavigationResource menuNavigationResource = MenuNavigationResource.of(menuNavigation, fullUrl);

        return TreeBranchResource.of(menuNavigation.getTreeId(), menuNavigation.getParentTreeId(), menuNavigationResource);
    }

    public boolean moveBranch(long projectId, long nodeId, String newParentId){
        Optional<MenuNavigation> menuNavigation = Optional.ofNullable(this.menuNavigationRepository.findByProjectIdAndId(projectId,nodeId));

        if(!menuNavigation.isPresent())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        MenuNavigation navigation = menuNavigation.get();
        navigation.setParentTreeId(newParentId);

        MenuNavigation parentMenuNavigation = this.menuNavigationRepository.findByProjectIdAndTreeId(projectId, newParentId);
        navigation.setBy(parentMenuNavigation);

        return true;
    }

    public boolean removeBranch(long projectId, long id) {
        MenuNavigation menuNavigation = menuNavigationRepository.findByProjectIdAndId(projectId, id);

        if(menuNavigation == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        menuNavigation.expire();

        List<AuthorityNavigationUnit> authorityNavigationUnits = authorityNavigationUnitRepository.findByStatusTrueAndMenuNavigationId(menuNavigation.getId());

        if(authorityNavigationUnits != null && !authorityNavigationUnits.isEmpty()){
            for(AuthorityNavigationUnit authorityNavigationUnit : authorityNavigationUnits){
                authorityNavigationUnit.expire();
            }

            authorityNavigationUnitRepository.save(authorityNavigationUnits);
        }

        menuNavigationRepository.save(menuNavigation);

        List<MenuNavigation> childMenuNavigationGroup = menuNavigation.getMenuNavigationGroup();

        if(childMenuNavigationGroup.size() > 0){
            for(MenuNavigation navigation : childMenuNavigationGroup){
                this.removeBranch(projectId, navigation.getId());
            }

        }

        return true;
    }

}
