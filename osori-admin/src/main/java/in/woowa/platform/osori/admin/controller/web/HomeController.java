package in.woowa.platform.osori.admin.controller.web;

import in.woowa.platform.osori.admin.commons.annotations.LoginCheck;
import in.woowa.platform.osori.admin.service.DashBoard;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * Created by seooseok on 2016. 9. 23..
 */
@LoginCheck
@Controller
public class HomeController {

    @Autowired
    private DashBoard dashBoard;

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        int countByLiveUser = dashBoard.countByLiveTotalUsers();
        int countByWaitUser =  dashBoard.countByLiveWaitUsers();

        model.addAttribute("countByLiveUser", countByLiveUser);
        model.addAttribute("countByWaitUser", countByWaitUser);

        return "dashboard";
    }
}
