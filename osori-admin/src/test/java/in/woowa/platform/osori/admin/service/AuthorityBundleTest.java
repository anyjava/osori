package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.data.resource.MenuNavigationResource;
import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.entity.MenuNavigationTest;
import in.woowa.platform.osori.admin.manager.AuthorityDefinitionManager;
import in.woowa.platform.osori.admin.repository.MenuNavigationRepository;
import org.junit.Assert;
import org.junit.Ignore;
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
 * Created by seooseok on 2016. 10. 4..
 */
@Service
@Transactional
@Rollback(value = false)
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
public class AuthorityBundleTest {

    @Autowired
    private AuthorityBundle authorityBundle;
    @Autowired
    private MenuNavigationTest menuNavigationTest;
    @Autowired
    private AuthorityDefinitionManager authorityDefinitionManager;
    @Autowired
    private MenuNavigationRepository menuNavigationRepository;

    @Test
    public void register() throws Exception {
        //Given
        MenuNavigation rootNavigation = menuNavigationTest.createMenuTree();
        List<MenuNavigation> menuNavigations = menuNavigationRepository.findByStatusTrueAndProjectId(rootNavigation.getProject().getId());
        long projectId = menuNavigations.get(0).getProject().getId();
        String name = "테스트 그룹";
        List<Long> targetNavigations = new ArrayList<>();
        menuNavigations.forEach(menuNavigation -> {targetNavigations.add(menuNavigation.getId());});

        //When
        long authorityDefinitionId = authorityBundle.createBundle(projectId, name, targetNavigations);

        //Then
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findByLive(authorityDefinitionId);
        Assert.assertNotNull(authorityDefinition);

        List<MenuNavigationResource> hasMenuNavigations = authorityBundle.lookInBundle(authorityDefinitionId);

        Assert.assertNotEquals("unit size", hasMenuNavigations.size(), 0);

    }

    @Test
    public void findByLive() throws Exception {
        //Given
        MenuNavigation rootNavigation = menuNavigationTest.createMenuTree();
        List<MenuNavigation> menuNavigations = menuNavigationRepository.findByStatusTrueAndProjectId(rootNavigation.getProject().getId());
        long projectId = menuNavigations.get(0).getProject().getId();
        String name = "테스트 그룹";
        List<Long> targetNavigations = new ArrayList<>();
        menuNavigations.forEach(menuNavigation -> {targetNavigations.add(menuNavigation.getId());});
        long authorityDefinitionId = authorityBundle.createBundle(projectId, name, targetNavigations);

        //When
        AuthorityDefinition authorityDefinition = authorityDefinitionManager.findByLive(authorityDefinitionId);

        //Then
        Assert.assertNotNull(authorityDefinition);
    }

    @Test
    public void addBy() throws Exception {
        //Given
        MenuNavigation rootNavigation = menuNavigationTest.createMenuTree();
        List<MenuNavigation> menuNavigations = menuNavigationRepository.findByStatusTrueAndProjectId(rootNavigation.getProject().getId());
        long projectId = menuNavigations.get(0).getProject().getId();
        String name = "테스트 그룹";

        List<Long> targetNavigations = new ArrayList<>();

        int limit = menuNavigations.size() / 2;

        for(int i = 0; i < limit ; i++)
            targetNavigations.add(menuNavigations.get(i).getId());

        long authorityDefinitionId = authorityBundle.createBundle(projectId, name, targetNavigations);

        List<Long> addNavigationIdGroup = new ArrayList<>();

        //첫번째 것을 대상으로 잡는다.
        addNavigationIdGroup.add(menuNavigations.get(0).getId());
        //마지막 꺼를 넣는 대상으로 잡는다.
        addNavigationIdGroup.add(menuNavigations.get(menuNavigations.size() - 1).getId());

        //When
        authorityBundle.addBy(authorityDefinitionId,addNavigationIdGroup);
        //Then
        List<MenuNavigationResource> authorityNavigations = authorityBundle.lookInBundle(authorityDefinitionId);

        Assert.assertEquals(limit + 1 , authorityNavigations.size());
    }

    @Test
    @Ignore
    @Deprecated
    public void removeBy() throws Exception {
        //Given
        MenuNavigation rootNavigation = menuNavigationTest.createMenuTree();
        List<MenuNavigation> menuNavigations = menuNavigationRepository.findByStatusTrueAndProjectId(rootNavigation.getProject().getId());
        long projectId = menuNavigations.get(0).getProject().getId();
        String name = "테스트 그룹";

        List<Long> targetNavigations = new ArrayList<>();

        int limit = menuNavigations.size() / 2;

        for(int i = 0; i < limit ; i++)
            targetNavigations.add(menuNavigations.get(i).getId());

        long authorityDefinitionId = authorityBundle.createBundle(projectId, name, targetNavigations);

        List<MenuNavigationResource> hasMenuNavigations = authorityBundle.lookInBundle(authorityDefinitionId);
        int hasMenuSize = hasMenuNavigations.size();
        //When

        List<Long> removeMenuNavigationIdGroup = new ArrayList<>();
        removeMenuNavigationIdGroup.add(hasMenuNavigations.get(hasMenuSize - 1).getId());
        removeMenuNavigationIdGroup.forEach(id -> {
            authorityBundle.expireBy(authorityDefinitionId, id);
        });


        //Then
        int removedSize = authorityBundle.lookInBundle(authorityDefinitionId).size();

        Assert.assertEquals(hasMenuSize, removedSize + 1);
    }

    @Test
    public void expire() throws Exception {
        //Given

        //When

        //Then
    }

}
