package in.woowa.platform.osori.admin.controller.web.projects;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.manager.MenuNavigationManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by HanJaehyun on 2016. 9. 27..
 */
@RestController
@RequestMapping(value = "/project/{projectId}")
public class NavigationController {

    @Autowired
    private MenuNavigationManager menuNavigationManager;

    @PutMapping("/navigation/{menuNavigationId}")
    @ResponseBody
    public ApiRes modifyInfo(@PathVariable long menuNavigationId
            , @RequestParam String name
            , @RequestParam OsoriConstant.NavigationType type
            , @RequestParam RequestMethod methodType
            , @RequestParam String uriBlock){

        menuNavigationManager.modify(menuNavigationId, name, type, methodType, uriBlock);

        return ApiRes.success();
    }
}
