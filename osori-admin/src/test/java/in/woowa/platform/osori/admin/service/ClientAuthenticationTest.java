package in.woowa.platform.osori.admin.service;

import org.springframework.util.AntPathMatcher;

import java.net.MalformedURLException;
import java.net.URISyntaxException;

/**
 * Created by seooseok on 2016. 10. 26..
 */
public class ClientAuthenticationTest {

    public static void main(String[] args) throws MalformedURLException, URISyntaxException {
        String url1 = "/management/user/{a}/grant/project/{b}/authority-personal/{c}";
        String url2 = "/management/user/grant/project/2/authority-personal/3";

        AntPathMatcher antPathMatcher = new AntPathMatcher();

        System.out.println(antPathMatcher.match(url1, url2));







    }



    public void createUrlTree(){



    }

}
