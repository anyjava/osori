package in.woowa.platform.osori.client.core;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.http.HttpException;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;

/**
 * Created by seooseok on 2016. 9. 29..
 * 오소리 API 연동 helper class
 */
public class HttpRequestHelper {

    private static final String DEFAULT_CHARSET = "UTF-8";

    protected static HttpUriRequest post(String url, List<NameValuePair> nameValuePairs) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(url);

        UrlEncodedFormEntity urlEncodedFormEntity = new UrlEncodedFormEntity(nameValuePairs);
        urlEncodedFormEntity.setContentEncoding(DEFAULT_CHARSET);

        post.setHeader(HttpHeaders.ACCEPT,"application/json");
        post.setHeader(HttpHeaders.ACCEPT_CHARSET, DEFAULT_CHARSET);
        post.setHeader(HttpHeaders.CONTENT_ENCODING, DEFAULT_CHARSET);
        post.setEntity(urlEncodedFormEntity);

        return post;
    }

    protected static HttpUriRequest post(String url, Map<String,Object> valuesMap) throws UnsupportedEncodingException {
        HttpPost post = new HttpPost(url);
        Gson gson = new Gson();
        String jsonString = gson.toJson(valuesMap);
        StringEntity stringEntity = new StringEntity(jsonString);
        stringEntity.setContentEncoding(DEFAULT_CHARSET);
        stringEntity.setContentType("application/json");

        post.setHeader(HttpHeaders.ACCEPT,"application/json");
        post.setHeader(HttpHeaders.ACCEPT_CHARSET, DEFAULT_CHARSET);
        post.setHeader(HttpHeaders.CONTENT_ENCODING, DEFAULT_CHARSET);
        post.setEntity(stringEntity);

        return post;
    }

    protected static HttpUriRequest get(String url, List<NameValuePair> nameValuePairs) throws UnsupportedEncodingException, URISyntaxException {
        HttpGet get = new HttpGet(url);

        URI uri = new URIBuilder(get.getURI()).setParameters(nameValuePairs).build();

        get.setHeader(HttpHeaders.ACCEPT,"application/json");
        get.setHeader(HttpHeaders.ACCEPT_CHARSET, DEFAULT_CHARSET);
        get.setHeader(HttpHeaders.CONTENT_ENCODING, DEFAULT_CHARSET);
        get.setURI(uri);

        return get;
    }

    protected static JsonElement getResponseElement(HttpResponse response) throws IOException, HttpException {

        int statusCode = response.getStatusLine().getStatusCode();
        String result = EntityUtils.toString(response.getEntity());

        JsonObject jsonObject = new JsonParser().parse(result).getAsJsonObject();
        String code = jsonObject.get("code").getAsString();
        String message = jsonObject.get("message").getAsString();

        if(200 > statusCode || statusCode >= 300 || !"0000".equals(code))
            throw new HttpException(message + "(" + code + ")");

        return jsonObject.get("result");
    }
}
