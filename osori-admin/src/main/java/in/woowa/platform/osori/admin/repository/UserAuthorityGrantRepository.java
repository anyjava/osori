package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.UserAuthorityGrant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Created by seooseok on 2016. 10. 5..
 */
public interface UserAuthorityGrantRepository extends JpaRepository<UserAuthorityGrant, Long> {

    @Query("SELECT DISTINCT uag FROM UserAuthorityGrant uag JOIN FETCH uag.user u JOIN FETCH uag.authorityDefinition ad JOIN FETCH ad.project WHERE uag.status = true AND uag.user.id = :userId")
    List<UserAuthorityGrant> findByStatusTrueAndUserId(@Param("userId") long userId);

}
