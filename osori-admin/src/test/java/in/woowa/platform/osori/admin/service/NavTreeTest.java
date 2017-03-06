package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.manager.ProjectManager;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by HanJaehyun on 2016. 9. 22..
 */
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
public class NavTreeTest {

    @Autowired
    private ProjectManager projectManager;

    @Autowired
    private NavigationTree navigationTree;

    /*@Before
    public void setUp() throws Exception {
        Project project1 = new Project("테스트프로젝트1", "테스트1", OsoriConstant.TypeAuthCheck.id, "test1");
        Project project2 = new Project("테스트프로젝트2", "테스트2", OsoriConstant.TypeAuthCheck.url, "test2");

        this.projectService.addBranch(project1);
        this.projectService.addBranch(project2);

        MenuNavigation menuNavigation = new MenuNavigation(project1, "부모메뉴", "/test1", category, 1, RequestMethod.GET);

        this.navigationTreeService.addBranch(menuNavigation);

    }
    @Ignore
    @Test
    public void addBranch() throws Exception {

    }*/



}
