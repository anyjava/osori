package in.woowa.platform.osori.admin.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

/**
 * Created by seooseok on 2016. 10. 19..
 */
@Data
@Builder
public class TreeBranchResource {

    @JsonProperty(value = "id")
    private String treeId;

    @JsonProperty(value = "parent")
    private String parentTreeId;

    private String text;

    private String type;

    @JsonProperty(value = "a_attr")
    private MenuNavigationResource menuNavigationResource;

    public static TreeBranchResource of(String treeId, String parentTreeId, MenuNavigationResource menuNavigationResource){
        return builder()
                .treeId(treeId)
                .parentTreeId(parentTreeId)
                .text(menuNavigationResource.getName())
                .type(menuNavigationResource.getType().name())
                .menuNavigationResource(menuNavigationResource)
                .build();
    }
}
