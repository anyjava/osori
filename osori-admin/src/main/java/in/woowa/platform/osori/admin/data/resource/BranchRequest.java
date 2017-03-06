package in.woowa.platform.osori.admin.data.resource;

import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.web.bind.annotation.RequestMethod;

import static in.woowa.platform.osori.admin.commons.constant.OsoriConstant.NavigationType;

/**
 * Created by HanJaehyun on 2016. 9. 27..
 */
@Data
public class BranchRequest {
    @NotEmpty
    private String treeId;
    @NotEmpty
    private String parentTreeId;
    @NotEmpty
    private String name;
    @NotEmpty
    private String uriBlock;

    private NavigationType type;
    private RequestMethod methodType;
}
