package in.woowa.platform.osori.admin.manager;

import in.woowa.platform.osori.admin.config.exception.HumanErr;
import in.woowa.platform.osori.admin.config.exception.ApplicationException;
import in.woowa.platform.osori.admin.config.exception.ProcessErr;
import in.woowa.platform.osori.admin.data.resource.UserResource;
import in.woowa.platform.osori.admin.entity.Project;
import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.entity.User.Status;
import in.woowa.platform.osori.admin.entity.UserAuthorityGrant;
import in.woowa.platform.osori.admin.entity.UserPersonalGrant;
import in.woowa.platform.osori.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by seooseok on 2016. 10. 5..
 */
@Service
public class UserManager {

    @Autowired
    private UserRepository userRepository;

    public User createBy(String email, String name){
        User user = User.of(email, name);
        return userRepository.save(user);
    }

    public User createBy(String email, String name, Status status){
        User user = User.of(email, name, status);
        return userRepository.save(user);
    }

    public User createBy(String email, String name, String department, boolean accessPrivacyInformation){
        User user = this.createBy(email, name, Status.allow);
        user.setDepartment(department);
        user.setPrivacy(accessPrivacyInformation);

        return userRepository.save(user);
    }

    public User findBy(long id){
        User user = userRepository.findOne(id);

        if(user == null)
            throw new ApplicationException(HumanErr.IS_EMPTY);

        return user;
    }

    public User findBy(String email){
        return userRepository.findByEmail(email);
    }

    public List<User> findBy(Status status){
        return this.findAll()
                .stream()
                .filter(user -> user.getStatus().equals(status))
                .collect(Collectors.toList());
    }

    public List<User> findByLive(){
        return this.findAll().stream()
                .filter(user -> user.getStatus() != Status.expire)
                .collect(Collectors.toList());
    }

    public User findByStatusAllow(long id){
        User user = this.findBy(id);

        if(!user.getStatus().equals(Status.allow))
            throw new IllegalArgumentException("해당 유저는 허가된 유저가 아닙니다");

        return user;
    }

    public List<UserResource> findAllByJoinedProjectUsers(long projectId){
        List<User> users = userRepository.findAllByJoinedProjectUsers(projectId);
        if(users == null)
            users = Collections.emptyList();

        List<UserResource> userResources = new ArrayList<>();

        users.forEach(user -> {
            userResources.add(UserResource.of(user));
        });

        return userResources;
    }

    public UserResource getUserDetail(long id){
        User user = this.findBy(id);

        List<Project> projects = user.getProjects();

        List<UserAuthorityGrant> userAuthorityGrants = user.getUserAuthorityGrants();

        List<UserPersonalGrant> userPersonalGrants = user.getUserPersonalGrants();

        return UserResource.ofDetail(user,projects, userAuthorityGrants, userPersonalGrants);
    }

    public void expireBy(List<Long> userIdGroup){
        userIdGroup.forEach(userId -> {
            User user = this.findBy(userId);
            user.expire();
        });
    }

    public void modifyBy(List<Long> userIdGroup, Status userStatus){
        if(Status.expire == userStatus)
            throw new ApplicationException(HumanErr.INVALID_ARGS,"만료의 경우 별도 만료 API를 이용해주세요.");

        userIdGroup.forEach(userId -> {
            User user = this.findBy(userId);

            if(Status.expire == user.getStatus())
                throw new ApplicationException(ProcessErr.ALREADY_EXPIRED,new Object[]{user.getEmail()});

            user.setStatus(userStatus);
        });
    }

    public void modifyBy(List<Long> userIdGroup, String department, boolean isPrivacy){
        userIdGroup.forEach(userId -> {
            User user = this.findBy(userId);
            user.setDepartment(department);
            user.setPrivacy(isPrivacy);
        });
    }

    public void saveBy(User user){
        userRepository.save(user);
    }

    public List<User> findAll() {
        List<User> users = userRepository.findAll();
        if(users == null)
            users = Collections.emptyList();

        return users;
    }
}
