package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>{

    @Query("SELECT count(u) FROM User u WHERE status IN :status")
    int countByClientUsersInStatus(@Param("status") List<User.Status> statusGroup);

    @Query("SELECT DISTINCT u " +
            "FROM AuthorityDefinition ad " +
            "JOIN ad.project pj " +
            "JOIN ad.userAuthorityGrants uag " +
            "JOIN uag.user u " +
            "WHERE pj.id = :projectId " +
            "AND u.status = 'allow' " +
            "AND ad.status = true " +
            "AND uag.status = true ")
    List<User> findAllByJoinedProjectUsers(@Param("projectId") long projectId);

    User findByEmail(String email);
}
