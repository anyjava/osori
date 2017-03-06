package in.woowa.platform.osori.client.core;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 9. 29..
 */
public class OsoriClientTest {

    private OsoriClient osoriClient;


    @Before
    public void setUp() throws MalformedURLException {
        this.osoriClient = new OsoriClient("http://localhost:8080","ae150d532953fcf0167b846283a07eb05ea8759889a919cf0ffaaa9cc516aa69");
    }


    @Test
    @Ignore
    public void authorityCheckByUri() throws Exception {




        //When

        String email = "sample-user1@woowahan.com";
        List<OsoriAccessUri> accessUris = new ArrayList<>();
        accessUris.add(new OsoriAccessUri(new URI("/management/smartmenu/store/13414/mapping/12312"), "GET"));

        OsoriClient osoriClient = new OsoriClient("http://osori.admin.woowa.in", "test");

        List<OsoriAccessRes> osoriAccessRes = osoriClient.authorizationCheckByUri(email,accessUris);



        //Then
    }

    @Test
    public void authorityCheckById() throws Exception {
        List<Long> idGroup = new ArrayList<>();
        //Given
        idGroup.add(1L);
        //idGroup.add(2L);
        //idGroup.add(3L);
        String email = "sample-user1@woowahan.com";
        //When
        /*
        List<Map<String,String>> map = this.osoriClient.authorizationCheckById(email, idGroup);

        System.out.println(map.toString());
        */
        //Then
    }

}
