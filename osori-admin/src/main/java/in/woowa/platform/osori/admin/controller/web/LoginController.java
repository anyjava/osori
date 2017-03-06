package in.woowa.platform.osori.admin.controller.web;

import in.woowa.platform.osori.admin.service.AdminLogin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.HttpSession;

/**
 * Created by HanJaehyun on 2016. 9. 22..
 */
@Controller
public class LoginController {

    @Autowired
    private HttpSession httpSession;

    @Autowired
    private AdminLogin adminLogin;

    @GetMapping("/logout")
    public String logout(){
        httpSession.invalidate();
        return "redirect:/login.html";
    }
}
