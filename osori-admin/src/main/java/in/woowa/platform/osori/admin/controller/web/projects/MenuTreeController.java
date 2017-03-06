package in.woowa.platform.osori.admin.controller.web.projects;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.data.resource.BranchRequest;
import in.woowa.platform.osori.admin.data.resource.TreeBranchResource;
import in.woowa.platform.osori.admin.service.NavigationTree;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.Valid;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 19..
 */
@Controller
@RequestMapping(value = "/project/{projectId}/menu-tree")
public class MenuTreeController {

    @Autowired
    private NavigationTree navigationTree;


    @PostMapping("/branch")
    @ResponseBody
    public ApiRes createBranch(@PathVariable long projectId, @Valid @ModelAttribute BranchRequest branchRequest){
        Long id = this.navigationTree.createBranch(projectId, branchRequest);

        return new ApiRes(id);
    }


    @PutMapping("/branch/{menuNavigationId}")
    @ResponseBody
    public ApiRes moveBranch(@PathVariable long projectId
            , @PathVariable long menuNavigationId
            , @RequestParam(value="parentTreeId") String parentTreeId){

        navigationTree.moveBranch(projectId, menuNavigationId, parentTreeId);

        return ApiRes.success();
    }


    @DeleteMapping("/branch/{menuNavigationId}")
    @ResponseBody
    public ApiRes deleteBranch(@PathVariable long projectId, @PathVariable long menuNavigationId){
        this.navigationTree.removeBranch(projectId, menuNavigationId);

        return ApiRes.success();
    }


    @GetMapping("/branch/{menuNavigationId}")
    @ResponseBody
    public ApiRes findBranch(@PathVariable long projectId, @PathVariable long menuNavigationId){

        TreeBranchResource treeBranchResource = navigationTree.getTreeBranch(projectId,menuNavigationId);

        return new ApiRes(treeBranchResource);
    }

    @GetMapping("/branches")
    @ResponseBody
    public ApiRes getAllBranch(@PathVariable long projectId){
        List<TreeBranchResource> treeBranchResources = navigationTree.getTreeBranches(projectId);

        return new ApiRes(treeBranchResources);
    }

}
