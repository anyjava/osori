package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by HanJaehyun on 2016. 9. 21..
 */
@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>{

    Project findByApiKey(String apiKey);
}
