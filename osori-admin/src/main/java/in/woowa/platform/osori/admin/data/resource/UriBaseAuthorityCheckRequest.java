package in.woowa.platform.osori.admin.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import org.hibernate.validator.constraints.NotEmpty;

import java.util.List;

/**
 * Created by seooseok on 2016. 10. 27..
 */
@Data
public class UriBaseAuthorityCheckRequest {
    @NotEmpty
    private String apiKey;
    @NotEmpty
    @JsonProperty("accessUris")
    private List<AccessUriRequest> accessUriRequests;
}
