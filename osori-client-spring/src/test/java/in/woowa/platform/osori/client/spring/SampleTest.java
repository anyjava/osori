package in.woowa.platform.osori.client.spring;

import in.woowa.platform.osori.client.core.OsoriClient;
import org.junit.Ignore;
import org.junit.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.security.GeneralSecurityException;

/**
 * Created by htwoh on 2016. 11. 10..
 */
public class SampleTest {

    @Test
    @Ignore
    public void test() throws IOException, URISyntaxException, GeneralSecurityException {
        String host = "http://127.0.0.1:8080";
        String apiKey = "ae150d532953fcf0167b846283a07eb05ea8759889a919cf0ffaaa9cc516aa69";
        String email = "sample-user1@woowahan.com";

        OsoriClient osoriClient = new OsoriClient(host, apiKey);
        /*
        AccessUri accessUri = new AccessUri(new URI("/api/user/"+email+"/authorization-check/url"), "GET");

        List<OsoriAccessRes> result = osoriClient.authorizationCheckByUri(email, Arrays.asList(accessUri));

        System.out.println(result.get(0).isAccessResult());
        */
    }
}

