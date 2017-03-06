package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.commons.utils.EncryptUtil;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.entity.Admin;
import in.woowa.platform.osori.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seooseok on 2016. 10. 18..
 */
@Service
@Transactional(readOnly = true)
public class AdminLogin {

    @Autowired
    private AdminRepository adminRepository;

    public Admin loginCheck(String email, String pw){
        pw = EncryptUtil.sha256(pw);
        Admin admin = adminRepository.findByEmailAndPw(email,pw);

        if(admin == null)
            throw new ApplicationException(HumanErr.FAIL_LOGIN);

        if(!admin.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE, "해당 유저는 로그인 권한이 만료되었습니다.");

        return admin;
    }
}
