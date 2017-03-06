package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.AuthorityDefinition;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by seooseok on 2016. 9. 29..
 * 권한 정의 레파지토리
 */
@Repository
public interface AuthorityDefinitionRepository extends JpaRepository<AuthorityDefinition, Long> {
    List<AuthorityDefinition> findByProjectIdAndStatusTrue(long prodectId);

    List<AuthorityDefinition> findByIdIn(List<Long> id);
}
