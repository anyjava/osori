package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.UserPersonalGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by seooseok on 2016. 10. 5..
 */
public interface UserPersonalGrantRepository extends JpaRepository<UserPersonalGrant, Long> {

    @Query("SELECT DISTINCT upg FROM UserPersonalGrant upg JOIN FETCH upg.user u JOIN FETCH upg.menuNavigation mn JOIN FETCH mn.project p WHERE upg.status = true AND upg.user.id = :userId")
    List<UserPersonalGrant> findByStatusTrueAndUserId(@Param("userId") long userId);
}
