package in.woowa.platform.osori.admin.manager;

import in.woowa.platform.osori.admin.commons.constant.OsoriConstant;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.entity.MenuNavigation;
import in.woowa.platform.osori.admin.repository.MenuNavigationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;
import java.util.Optional;

/**
 * Created by seooseok on 2016. 10. 11..
 */
@Service
public class MenuNavigationManager {

    @Autowired
    private MenuNavigationRepository menuNavigationRepository;

    public List<MenuNavigation> findBy(List<Long> menuNavigationIdGroup){
        return menuNavigationRepository.findByIdIn(menuNavigationIdGroup);
    }

    public List<MenuNavigation> findByLive(List<Long> menuNavigationIdGroup){
        List<MenuNavigation> menuNavigations = this.findBy(menuNavigationIdGroup);

        menuNavigations.forEach(menuNavigation -> {
            if(!menuNavigation.isStatus())
                throw new ApplicationException(HumanErr.IS_EXPIRE);
        });

        return menuNavigations;
    }

    public MenuNavigation findBy(long projectId, String treeId){
        Optional<MenuNavigation> menuNavigation = Optional.ofNullable(this.menuNavigationRepository.findByProjectIdAndTreeId(projectId, treeId));

        if(!menuNavigation.isPresent())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        return menuNavigation.get();
    }

    public MenuNavigation findBy(long projectId, long nodeId){
        Optional<MenuNavigation> menuNavigation = Optional.ofNullable(this.menuNavigationRepository.findByProjectIdAndId(projectId, nodeId));

        if(!menuNavigation.isPresent())
            throw new ApplicationException(HumanErr.IS_EMPTY);

        return menuNavigation.get();
    }

    public List<MenuNavigation> findByLive(long projectId, List<Long> menuNavigationIdGroup){
        return menuNavigationRepository.findByStatusTrueAndProjectIdAndIdIn(projectId, menuNavigationIdGroup);
    }

    public MenuNavigation findByLive(long menuNavigationId){
        MenuNavigation menuNavigation = menuNavigationRepository.findOne(menuNavigationId);

        if(menuNavigation == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        if(!menuNavigation.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE);

        return menuNavigation;
    }

    public String getFullUrl(MenuNavigation menuNavigation){
        if("#".equals(menuNavigation.getParentTreeId()))
            return menuNavigation.getUriBlock();
        else
            return getFullUrl(menuNavigation.getParentMenuNavigation()) + menuNavigation.getUriBlock();
    }

    public void modify(long menuNavigationId, String name, OsoriConstant.NavigationType type, RequestMethod methodType, String uriBlock){
        MenuNavigation menuNavigation = this.findByLive(menuNavigationId);

        menuNavigation.setType(type);
        menuNavigation.setName(name);
        menuNavigation.setUriBlock(uriBlock);
        menuNavigation.setMethodType(methodType);

        menuNavigationRepository.save(menuNavigation);
    }

}
