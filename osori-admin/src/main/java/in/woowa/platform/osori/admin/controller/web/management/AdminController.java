package in.woowa.platform.osori.admin.controller.web.management;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import in.woowa.platform.osori.admin.data.OsoriSessionInfo;
import in.woowa.platform.osori.admin.data.resource.AdminResource;
import in.woowa.platform.osori.admin.entity.Admin;
import in.woowa.platform.osori.admin.manager.AdminManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by seooseok on 2016. 10. 18..
 */
@Controller
@RequestMapping("/management/admin")
public class AdminController {

    @Autowired
    private AdminManager adminManager;

    @GetMapping("")
    @ResponseBody
    public ApiRes findOne(HttpServletRequest request){
        OsoriSessionInfo osoriSessionInfo = RequestHelper.getSessionByAdmin(request);

        Admin admin = adminManager.findBy(osoriSessionInfo.getAdminId());

        AdminResource adminResource = AdminResource.of(admin);

        return new ApiRes(adminResource);
    }


    //NOTI: admin의 경우 별도 ID를 받지 않는데 이는 profile 수정의 대상은 무조건 로그인 된 대상만 처리 할 수 있도록 하기 위함이다.
    @PutMapping("")
    @ResponseBody
    public ApiRes modifyInfo(HttpServletRequest request
            , @RequestParam(required = false) String pw
            , @RequestParam(required = false) String imageUrl
            , @RequestParam(required = false) String description){

        OsoriSessionInfo osoriSessionInfo = RequestHelper.getSessionByAdmin(request);

        adminManager.modifyBy(osoriSessionInfo.getAdminId(), pw, imageUrl, description);
        return ApiRes.success();
    }
}
