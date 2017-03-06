package in.woowa.platform.osori.admin.entity;

import in.woowa.platform.osori.admin.MenuNavigationResource;
import in.woowa.platform.osori.admin.repository.MenuNavigationRepository;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static in.woowa.platform.osori.admin.commons.constant.OsoriConstant.NavigationType.category;
import static org.springframework.web.bind.annotation.RequestMethod.GET;

/**
 * Created by seooseok on 2016. 9. 23..
 */
@Service
@AutoConfigureDataJpa
@ActiveProfiles("default")
@RunWith(SpringRunner.class)
@SpringBootTest
public class MenuNavigationTest {

    @Autowired
    private ProjectTest projectTest;
    @Autowired
    private MenuNavigationRepository menuNavigationRepository;

    @Test
    public void createMenuNaviTree() throws Exception{

        MenuNavigation menuNavigation = this.createMenuTree();
        Assert.assertNotNull(menuNavigation.getId());
        Assert.assertThat(menuNavigation.getMenuNavigationGroup().size(), CoreMatchers.is(2));
    }


    public MenuNavigation createMenuTree(){
        Project project = projectTest.createByProject();

        MenuNavigation rootNavigation = new MenuNavigation(project, "root", "#", "루트", "/management", category, GET);
        MenuNavigation childNavigation1 = new MenuNavigation(project, "child1", "root", "트리1", "/{userId}", category, GET);
        MenuNavigation childNavigation2 = new MenuNavigation(project, "child2", "root", "트리2", "/{userId}", category, GET);
        MenuNavigation microNavigation1 = new MenuNavigation(project, "micro1", "child1", "노드1", "/download", category, GET);
        MenuNavigation microNavigation2 = new MenuNavigation(project, "micro2", "child2", "노드2", "/download", category, GET);

        microNavigation1.setBy(childNavigation1);
        childNavigation1.addBy(microNavigation2);

        rootNavigation.addBy(childNavigation1);
        rootNavigation.addBy(childNavigation2);


        return menuNavigationRepository.save(rootNavigation);
    }

    @Test
    @Transactional
    public void selectByMenuNavi(){
        MenuNavigation menuNavigation = menuNavigationRepository.findOne(7L);
        System.out.println(menuNavigation.getName());

        List<MenuNavigation> menuNavigationGroup = menuNavigation.getMenuNavigationGroup();

        System.out.println(menuNavigationGroup.size());
    }

    @Test
    @Transactional
    public void selectByMenuNaviTree(){
        List<MenuNavigationResource> resources = this.getTree(1L);

        System.out.println(resources.size());
    }

    public List<MenuNavigationResource> getTree(long id){
        List<MenuNavigationResource> resources = new ArrayList<>();
        List<MenuNavigation> menuNavigations = Collections.synchronizedList(new ArrayList<>());
        MenuNavigation menuNavigation = menuNavigationRepository.findOne(id);

        menuNavigations.add(menuNavigation);

        this.findBy(menuNavigations, menuNavigation);

        for(MenuNavigation m : menuNavigations){
            resources.add(new MenuNavigationResource(m.getId(), m.getName(), m.getParentMenuNavigation().getId()));
        }

        return resources;
    }

    public void findBy(List<MenuNavigation> menuNavigations, MenuNavigation menuNavigation){
        if(menuNavigation.getMenuNavigationGroup().size() > 0){
            for(MenuNavigation m : menuNavigation.getMenuNavigationGroup()){
                menuNavigations.add(m);
                this.findBy(menuNavigations, m);
            }
        }
    }

}
