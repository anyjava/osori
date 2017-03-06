package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.repository.AuthorityDefinitionRepository;
import in.woowa.platform.osori.admin.repository.AuthorityNavigationUnitRepository;
import in.woowa.platform.osori.admin.repository.MenuNavigationRepository;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 9. 29..
 */
@Service
@Transactional
@Rollback(value = false)
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityDefinitionTest {

    @Autowired
    private ProjectTest projectTest;
    @Autowired
    private MenuNavigationTest menuNavigationTest;
    @Autowired
    private MenuNavigationRepository menuNavigationRepository;
    @Autowired
    private AuthorityDefinitionRepository authorityDefinitionRepository;
    @Autowired
    private AuthorityNavigationUnitRepository authorityNavigationUnitRepository;


    @Test
    public void expireTest() throws Exception {
        //Given
        AuthorityDefinition authorityDefinition = createAuthorityDefinitionAndSetupMenuNavigation();

        List<AuthorityNavigationUnit> authorityNavigationUnits = authorityNavigationUnitRepository.findByStatusTrueAndAuthorityDefinitionId(authorityDefinition.getId());

        List<Long> naviGroup = new ArrayList<>();

        naviGroup.add(authorityNavigationUnits.get(authorityNavigationUnits.size() - 1).getId());

        List<AuthorityNavigationUnit> units = authorityNavigationUnitRepository.findByStatusTrueAndAuthorityDefinitionIdAndMenuNavigationIdIn(authorityDefinition.getId(), naviGroup);

        units.forEach(unit -> unit.expire());

        authorityNavigationUnitRepository.save(units);

    }

    @Test
    public void createTest() throws Exception{
        AuthorityDefinition authorityDefinition = this.createAuthorityDefinition();
        Assert.assertNotNull(authorityDefinition.getId());
    }

    AuthorityDefinition createAuthorityDefinitionAndSetupMenuNavigation(){
        AuthorityDefinition authorityDefinition = this.createAuthorityDefinition();
        MenuNavigation rootNavigation = menuNavigationTest.createMenuTree();

        //When
        List<MenuNavigation> menuNavigations = menuNavigationRepository.findByStatusTrueAndProjectId(rootNavigation.getProject().getId());

        for(MenuNavigation menuNavigation : menuNavigations){
            authorityNavigationUnitRepository.save(new AuthorityNavigationUnit(authorityDefinition, menuNavigation));
        }

        return authorityDefinitionRepository.save(authorityDefinition);
    }

    AuthorityDefinition createAuthorityDefinition(){
        Project project = projectTest.createByProject();
        AuthorityDefinition authorityDefinition = AuthorityDefinition.of(project, "테스트 관리자 권한 그룹");

        return authorityDefinitionRepository.save(authorityDefinition);
    }

}
