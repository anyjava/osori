package in.woowa.platform.osori.admin.controller.api;

import in.woowa.platform.osori.admin.commons.ApiRes;
import in.woowa.platform.osori.admin.commons.utils.RequestHelper;
import in.woowa.platform.osori.admin.data.OsoriSessionInfo;
import in.woowa.platform.osori.admin.entity.Admin;
import in.woowa.platform.osori.admin.service.AdminLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by htwoh on 2017. 3. 2..
 */
@RestController
public class LoginApiController {
    @Autowired
    private HttpSession httpSession;

    @Autowired
    private AdminLogin adminLogin;

    @PostMapping("/check-login")
    public ApiRes checkLogin(String email, String password){

        Admin admin = adminLogin.loginCheck(email,password);

        OsoriSessionInfo osoriSessionInfo = OsoriSessionInfo.of(admin);

        RequestHelper.addSessionBy(httpSession, osoriSessionInfo);

        return new ApiRes(admin.getEmail());
    }
}
