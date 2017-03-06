package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.repository.ProjectRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seooseok on 2016. 9. 23..
 */
@Service
@Transactional
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProjectTest {

    @Autowired
    private ProjectRepository projectRepository;

    @Test
    public void saveTest() throws Exception{
        Long id = this.createByProject().getId();

        Assert.assertNotNull(id);
    }

    Project createByProject(){
        Project project = Project.of("테스트","테스트");
        return projectRepository.save(project);
    }

}
