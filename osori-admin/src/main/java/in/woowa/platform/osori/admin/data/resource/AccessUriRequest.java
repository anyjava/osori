package in.woowa.platform.osori.admin.data.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import in.woowa.platform.osori.admin.commons.utils.DecryptUtil;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestMethod;

import java.security.GeneralSecurityException;

/**
 * Created by seooseok on 2016. 10. 27..
 */
@Data
public class AccessUriRequest {
    @JsonProperty("uri")
    private String encryptedUri;

    private String methodType;

    public RequestMethod getRequestMethod(){
        return RequestMethod.valueOf(methodType);
    }

    public String getDecryptedUri(String decryptKey) throws GeneralSecurityException {
        return DecryptUtil.aes128(decryptKey, this.encryptedUri);
    }
}
