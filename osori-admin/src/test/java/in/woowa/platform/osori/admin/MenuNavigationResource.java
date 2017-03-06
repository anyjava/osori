package in.woowa.platform.osori.admin;

import lombok.Data;

/**
 * Created by seooseok on 2016. 9. 23..
 */
@Data
public class MenuNavigationResource {

    private long id;
    private String name;
    private Long parentId;

    public MenuNavigationResource(long id, String name, Long parentId){
        this.id = id;
        this.name = name;
        this.parentId = parentId;
    }

}
