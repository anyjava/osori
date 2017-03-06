package in.woowa.platform.osori.admin.controller.web.management;

import com.fasterxml.jackson.annotation.JsonView;
import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.data.JView;
import in.woowa.platform.osori.admin.data.resource.UserResource;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.manager.UserManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 6..
 */
@RestController
@RequestMapping("/management/users")
public class UsersController {

    @Autowired
    private UserManager userManager;

    @JsonView(JView.Base.class)
    @GetMapping("")
    public ApiRes findAll(@RequestParam(required = false) String status){
        List<User> userGroup;

        if(StringUtils.isEmpty(status))
            userGroup = userManager.findByLive();
        else
            userGroup = userManager.findBy(User.Status.valueOf(status));

        List<UserResource> userResources = new ArrayList<>();

        for(User user : userGroup){
            userResources.add(UserResource.of(user));
        }

        return new ApiRes(userResources);
    }


    @PutMapping("/{userIdGroup}")
    public ApiRes modifyInfo(@PathVariable List<Long> userIdGroup,
                             @RequestParam(required = false) String department,
                             @RequestParam(required = false) boolean isPrivacy,
                             @RequestParam(required = false) String status){
        if(StringUtils.isEmpty(status))
            userManager.modifyBy(userIdGroup, department, isPrivacy);
        else
            userManager.modifyBy(userIdGroup, User.Status.valueOf(status));

        return ApiRes.success();
    }


    @JsonView(JView.Base.class)
    @DeleteMapping("/{userIdGroup}")
    public ApiRes expireStatus(@PathVariable List<Long> userIdGroup ){
        userManager.expireBy(userIdGroup);
        return ApiRes.success();
    }
}
