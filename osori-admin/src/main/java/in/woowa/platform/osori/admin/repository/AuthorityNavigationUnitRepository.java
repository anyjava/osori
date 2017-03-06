package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.AuthorityNavigationUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by seooseok on 2016. 9. 30..
 */
@Repository
public interface AuthorityNavigationUnitRepository extends JpaRepository<AuthorityNavigationUnit, Long> {

    AuthorityNavigationUnit findByStatusTrueAndAuthorityDefinitionIdAndMenuNavigationId(long authorityDefinitionId, long menuNavigationId);

    List<AuthorityNavigationUnit> findByStatusTrueAndAuthorityDefinitionIdAndMenuNavigationIdIn(long authorityDefinitionId, List<Long> menuNavigationIdGroup);

    List<AuthorityNavigationUnit> findByStatusTrueAndMenuNavigationId(long menuNavigationId);

    @Query("SELECT DISTINCT an " +
            " FROM AuthorityNavigationUnit an JOIN FETCH an.authorityDefinition ad JOIN FETCH an.menuNavigation mn " +
            "WHERE an.status = true " +
            "AND an.authorityDefinition.id = :authorityDefinitionId")
    List<AuthorityNavigationUnit> findByStatusTrueAndAuthorityDefinitionId(@Param("authorityDefinitionId") long authorityDefinitionId);

    @Query("SELECT DISTINCT an " +
            "FROM AuthorityNavigationUnit an JOIN FETCH an.authorityDefinition ad JOIN FETCH an.menuNavigation mn " +
            "WHERE an.status = true " +
            "AND an.authorityDefinition.id in (:authorityDefinitionIdGroup)")
    List<AuthorityNavigationUnit> findByStatusTrueAndAuthorityDefinitionIdGroup(@Param("authorityDefinitionIdGroup") List<Long> authorityDefinitionIdGroup);
}
