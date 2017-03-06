package in.woowa.platform.osori.admin.data.resource;

import com.google.common.base.Strings;
import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import lombok.Builder;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by seooseok on 2016. 10. 19..
 */
@Data
@Builder
public class MenuNavigationResource {

    private long id;

    private OsoriConstant.NavigationType type;

    private String name;

    private String uriBlock;

    private RequestMethod methodType;

    private String fullUrl;

    public static MenuNavigationResource of(MenuNavigation menuNavigation, String fullUrl){
        if(!Strings.isNullOrEmpty(fullUrl))
            fullUrl = fullUrl.replaceAll("//*","/");

        return builder()
                .id(menuNavigation.getId())
                .type(menuNavigation.getType())
                .name(menuNavigation.getName())
                .uriBlock(menuNavigation.getUriBlock())
                .methodType(menuNavigation.getMethodType())
                .fullUrl(fullUrl)
                .build();
    }
}
