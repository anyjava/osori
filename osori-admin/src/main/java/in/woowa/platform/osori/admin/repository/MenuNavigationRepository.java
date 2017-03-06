package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.MenuNavigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by HanJaehyun on 2016. 9. 22..
 */
@Repository
public interface MenuNavigationRepository extends JpaRepository<MenuNavigation, Long>{
    MenuNavigation findByProjectIdAndId(long projectId, long nodeId);

    MenuNavigation findByProjectIdAndTreeId(long projectId, String treeId);

    List<MenuNavigation> findByStatusTrueAndProjectId(long projectId);

    List<MenuNavigation> findByStatusTrueAndProjectIdAndIdIn(long projectId, List<Long> menuNavigationId);

    @Query("SELECT DISTINCT mn FROM MenuNavigation mn JOIN FETCH mn.project pmn WHERE mn.id in (:menuNavigationIdGroup)")
    List<MenuNavigation> findByIdIn(@Param(value = "menuNavigationIdGroup") List<Long> menuNavigationIdGroup);
}
