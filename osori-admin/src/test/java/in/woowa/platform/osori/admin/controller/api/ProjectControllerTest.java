package in.woowa.platform.osori.admin.controller.api;

import in.woowa.platform.osori.admin.controller.web.projects.ProjectController;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by HanJaehyun on 2016. 9. 23..
 */
@AutoConfigureDataJpa
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@WebMvcTest(ProjectController.class)
public class ProjectControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Before
    public void setUp(){

    }

    @Test
    @Ignore
    public void test_프로젝트생성() throws Exception {

        MultiValueMap multiValueMap = new LinkedMultiValueMap();
        multiValueMap.add("name", "프로젝트1");
        multiValueMap.add("description", "프로젝트설명1");
        multiValueMap.add("apiKey", "1231231");

        this.mockMvc.perform(post("/api/project/addBranch").params(multiValueMap).accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"result\":1")));

    }

    @Test
    public void test_프로젝트호출() throws Exception {
        this.mockMvc.perform(get("/api/project/1").accept(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());
    }

}
