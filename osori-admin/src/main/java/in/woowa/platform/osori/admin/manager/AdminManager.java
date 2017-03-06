package in.woowa.platform.osori.admin.manager;

import com.google.common.base.Strings;
import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.entity.Admin;
import in.woowa.platform.osori.admin.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by seooseok on 2016. 10. 12..
 */
@Service
@Transactional
public class AdminManager {

    @Autowired
    private AdminRepository adminRepository;


    public Admin register(String email, String pw, String name, String description, String img){
        Admin admin = Admin.of(email,pw, name, description, img);

        return adminRepository.save(admin);
    }

    public void modifyBy(long adminId, String pw, String imageUrl, String description){
        Admin admin = adminRepository.findOne(adminId);

        if(admin == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        if(!admin.isStatus())
            throw new ApplicationException(HumanErr.IS_EXPIRE);

        if(!Strings.isNullOrEmpty(pw))
            admin.setPw(pw);

        if(!Strings.isNullOrEmpty(imageUrl))
            admin.setImg(imageUrl);

        if(!Strings.isNullOrEmpty(description))
            admin.setDescription(description);

        adminRepository.save(admin);
    }

    public Admin findBy(long id){
        return adminRepository.findOne(id);
    }


}
