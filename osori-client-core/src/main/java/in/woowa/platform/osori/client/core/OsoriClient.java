package in.woowa.platform.osori.client.core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import in.woowa.platform.osori.client.core.common.utils.EncryptUtil;
import org.apache.http.HttpException;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by seooseok on 2016. 9. 28..
 */
public class OsoriClient {

    private final OsoriConfiguration osoriConfiguration;
    private HttpClient httpClient;


    public OsoriClient(OsoriConfiguration osoriConfiguration){
        this.osoriConfiguration = osoriConfiguration;
        setupHttpClient(osoriConfiguration);
    }

    public OsoriClient(URL osoriUrl, String apiKey){
        OsoriConfiguration osoriConfiguration = new OsoriConfiguration.Builder(osoriUrl, apiKey).build();
        this.osoriConfiguration = osoriConfiguration;
        setupHttpClient(osoriConfiguration);
    }

    public OsoriClient(String osoriUrl, String apiKey) throws MalformedURLException {
        URL url = new URL(osoriUrl);

        OsoriConfiguration osoriConfiguration = new OsoriConfiguration.Builder(url, apiKey).build();
        this.osoriConfiguration = osoriConfiguration;
        setupHttpClient(osoriConfiguration);
    }

    private void setupHttpClient(OsoriConfiguration osoriConfiguration){
        CloseableHttpClient httpClient = HttpClients.custom()
                .setConnectionManager(osoriConfiguration.getPoolingHttpClientConnectionManager())
                .setDefaultRequestConfig(osoriConfiguration.getRequestConfig())
                .build();
        this.httpClient = httpClient;
    }


    /**
     * URI로 접근 권한 체크
     * @param accessUris 접근 권한을 체크 할 URI 그룹
     * @param email 접근 하는 사용자의 이메일
     * @return List { 권한 체크[authority: true, false], 권한 체크 방법[checkWay:uri], 권한 체크 대상[source:${uri}] }
     * @throws GeneralSecurityException Security algorithm이 aes, sha-256을 지원하지 않는 경우 발생
     * @throws IOException Http response가 json 형태가 아니거나 지원하지 않는 encoding으로 응답이 오는 경우 발생
     * @throws HttpException 권한 요청 응답갑이 정상(http status 2xx, api response code 0000)이 아닌 경우 발생
     */
    public List<OsoriAccessRes> authorizationCheckByUri(String email, List<OsoriAccessUri> accessUris) throws GeneralSecurityException, IOException, HttpException {

        String key = this.osoriConfiguration.getApiKey().substring(0,16);
        String encryptEmail = EncryptUtil.aes128(key, email);
        String osoriUrl = this.osoriConfiguration.getHost() + "/api/user/"+ encryptEmail +"/authorization-check/uri";

        List<Map<String,String>> accessUriParams = new ArrayList<>();
        for(OsoriAccessUri accessUri : accessUris){
            Map<String,String> urlMap = new HashMap<>();
            urlMap.put("uri", EncryptUtil.aes128(key, accessUri.getUri().toString()));
            urlMap.put("methodType", accessUri.getMethodType());

            accessUriParams.add(urlMap);
        }

        Map<String, Object> paramMap = new HashMap<>();

        paramMap.put("apiKey", this.osoriConfiguration.getApiKey());
        paramMap.put("accessUris", accessUriParams);

        HttpResponse response = this.httpClient.execute(HttpRequestHelper.post(osoriUrl, paramMap));

        List<OsoriAccessRes> resultMapGroup = this.convertResponseToClass(response);

        return resultMapGroup;
    }


    /**
     * ID로 접근 권한 체크
     * @param idGroup 접근 권한을 체크 할 ID 그룹
     * @param email 접근 하는 사용자의 이메일
     * @return List { 권한 체크[authority: true, false], 권한 체크 방법[checkWay:id], 권한 체크 대상[source:${id}] }
     * @throws GeneralSecurityException Security algorithm이 aes, sha-256을 지원하지 않는 경우 발생
     * @throws IOException Http response가 json 형태가 아니거나 지원하지 않는 encoding으로 응답이 오는 경우 발생
     * @throws HttpException 권한 요청 응답갑이 정상(http status 2xx, api response code 0000)이 아닌 경우 발생
     */
    public List<OsoriAccessRes> authorizationCheckById(String email, List<Integer> idGroup) throws GeneralSecurityException, IOException, HttpException {
        List<NameValuePair> nameValuePairs = new ArrayList<>();

        String key = this.osoriConfiguration.getApiKey().substring(0,16);
        String encryptEmail = EncryptUtil.aes128(key, email);
        String osoriUrl = this.osoriConfiguration.getHost() + "/api/user/"+ encryptEmail +"/authorization-check/id";

        for(Integer id : idGroup)
            if(id != 0)
                nameValuePairs.add(new BasicNameValuePair("id",id.toString()));

        nameValuePairs.add(new BasicNameValuePair("apiKey",this.osoriConfiguration.getApiKey()));

        HttpResponse response = this.httpClient.execute(HttpRequestHelper.post(osoriUrl, nameValuePairs));

        List<OsoriAccessRes> resultMapGroup = this.convertResponseToClass(response);

        return resultMapGroup;
    }

    public List<OsoriAccessUri> selectAccessibleUrls(String email, long projectId, String menuType) throws GeneralSecurityException, IOException, URISyntaxException, HttpException {
        String key = this.osoriConfiguration.getApiKey().substring(0,16);
        String encryptEmail = EncryptUtil.aes128(key, email);

        String osoriUrl = this.osoriConfiguration.getHost() + "/api/user/"+ encryptEmail +"/project/" + projectId + "/urls";

        List<NameValuePair> nameValuePairs = new ArrayList<>();
        nameValuePairs.add(new BasicNameValuePair("type", menuType));
        nameValuePairs.add(new BasicNameValuePair("apiKey", this.osoriConfiguration.getApiKey()));

        HttpResponse response = this.httpClient.execute(HttpRequestHelper.get(osoriUrl, nameValuePairs));

        List<Map<String, String>> result = this.convertResponseToMap(response);

        List<OsoriAccessUri> osoriAccessUris = new ArrayList<>();
        for (Map<String, String> uri : result) {
            osoriAccessUris.add(new OsoriAccessUri(new URI(uri.get("uri")), uri.get("requestMethod")));
        }

        return osoriAccessUris;
    }

    private List convertResponseToList(HttpResponse response) throws IOException, HttpException {
        JsonElement element = HttpRequestHelper.getResponseElement(response);

        Type type = new TypeToken<List>(){}.getType();

        return new Gson().fromJson(element, type);
    }

    private List<Map<String, String>> convertResponseToMap(HttpResponse response) throws IOException, HttpException {
        JsonElement element = HttpRequestHelper.getResponseElement(response);

        Type type = new TypeToken<List<Map<String, String>>>(){}.getType();

        return new Gson().fromJson(element, type);
    }

    private List<OsoriAccessRes> convertResponseToClass(HttpResponse response) throws IOException, HttpException {
        JsonElement element = HttpRequestHelper.getResponseElement(response);

        Type type = new TypeToken<List<OsoriAccessRes>>(){}.getType();

        return new Gson().fromJson(element, type);
    }
}
