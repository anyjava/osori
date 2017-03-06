package in.woowa.platform.osori.admin.controller.web.projects;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.data.resource.AuthGroup;
import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.data.resource.TreeBranchResource;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.service.AuthorityBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * Created by seooseok on 2016. 9. 30..
 */
@RestController
@RequestMapping("/project")
public class AuthorityController {

    @Autowired
    private AuthorityBundle authorityBundle;

    @RequestMapping(value = "/{projectId}/authority-bundle", method = POST)
    @ResponseBody
    public ApiRes create(@PathVariable long projectId, @RequestParam String groupName, @RequestParam("naviId[]") List<Long> naviIdGroup){

        long defineId = authorityBundle.createBundle(projectId, groupName, naviIdGroup);

        return new ApiRes(defineId);
    }

    @RequestMapping(value = "/{projectId}/authority-bundles", method = GET)
    @ResponseBody
    public ApiRes findAll(@PathVariable long projectId){
        List<AuthorityDefinition> authorityDefinitionList = authorityBundle.getBundles(projectId);

        List<AuthGroup> authGroup = authorityDefinitionList.stream().map(AuthGroup::of).collect(Collectors.toList());

        return new ApiRes(authGroup);
    }

    @RequestMapping(value = "/{projectId}/authority-bundle/{authId}/navigations", method = GET)
    @ResponseBody
    public ApiRes findBundlesNavigations(@PathVariable long projectId, @PathVariable long authId){
        List<MenuNavigationResource> menuNavigations = authorityBundle.lookInBundle(authId);

        return new ApiRes(menuNavigations);
    }

    @RequestMapping(value = "/{projectId}/authority-bundle/{authId}/branches", method = GET)
    @ResponseBody
    public ApiRes findBundlesBranches(@PathVariable long projectId, @PathVariable long authId){
        List<TreeBranchResource> treeBranchResources = authorityBundle.lookInBundleForTreeFormat(authId);

        return new ApiRes(treeBranchResources);
    }

    @RequestMapping(value = "/{projectId}/authority-bundle/{authId}", method = PUT)
    @ResponseBody
    public ApiRes modifyInfo(@PathVariable long projectId, @PathVariable long authId, @RequestParam long naviId){
        authorityBundle.modifyBundlesNavigation(projectId, authId, naviId);

        return ApiRes.success();
    }

    @RequestMapping(value = "/{projectId}/authority-bundle/{authId}", method = DELETE)
    @ResponseBody
    public ApiRes expire(@PathVariable long projectId, @PathVariable long authId){
        authorityBundle.expireBy(projectId, authId);

        return ApiRes.success();
    }

    @RequestMapping(value = "/{projectId}/authority-bundle/{authId}/navigations/{naviIdGroup}", method = DELETE)
    @ResponseBody
    public ApiRes expireNavigations(@PathVariable long projectId,
                                    @PathVariable long authId,
                                    @PathVariable("naviIdGroup") List<Long> naviIdGroup){

        authorityBundle.expireAuthorityNavigation(projectId, authId, naviIdGroup);

        return ApiRes.success();
    }

}
