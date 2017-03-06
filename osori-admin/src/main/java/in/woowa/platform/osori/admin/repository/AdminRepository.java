package in.woowa.platform.osori.admin.repository;

import in.woowa.platform.osori.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by HanJaehyun on 2016. 9. 20..
 */
@Repository
public interface AdminRepository extends JpaRepository<Admin, Long>{
    Admin findByEmailAndPw(String email, String pw);
}

