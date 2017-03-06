package in.woowa.platform.osori.admin.data.resource;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.data.AccessUri;
import lombok.Builder;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 10. 27..
 */
@Builder
@Getter
public class AuthenticationCheckResource {
    private String email;
    private String apiKey;
    private List<Long> menuNavigationIdGroup;
    private List<AccessUri> accessUriGroup;
    private OsoriConstant.AuthenticationCheckType authenticationCheckType;

    public static AuthenticationCheckResource ofIdBase(String email, String apiKey, List<Long> menuNavigationIdGroup){
        return builder()
                .email(email)
                .apiKey(apiKey)
                .menuNavigationIdGroup(menuNavigationIdGroup)
                .authenticationCheckType(OsoriConstant.AuthenticationCheckType.id)
                .build();
    }

    public static AuthenticationCheckResource ofUrlBase(String email, String apiKey, List<AccessUri> accessUriGroup){
        return builder()
                .email(email)
                .apiKey(apiKey)
                .accessUriGroup(accessUriGroup)
                .authenticationCheckType(OsoriConstant.AuthenticationCheckType.uri)
                .build();
    }

    public List<String> getUris(){
        if(this.authenticationCheckType != OsoriConstant.AuthenticationCheckType.uri)
            throw new IllegalArgumentException("authentication check type is wrong");

        return accessUriGroup.stream().map(AccessUri::getUri).collect(Collectors.toList());
    }



}
