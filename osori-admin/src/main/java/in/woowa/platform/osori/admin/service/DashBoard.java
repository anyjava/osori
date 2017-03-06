package in.woowa.platform.osori.admin.service;

import in.woowa.platform.osori.admin.entity.User;
import in.woowa.platform.osori.admin.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by seooseok on 2016. 10. 5..
 */
@Service
public class DashBoard {

    @Autowired
    private UserRepository userRepository;

    public int countByLiveTotalUsers(){
        List<User.Status> statusGroup = new ArrayList<>();
        statusGroup.add(User.Status.allow);
        statusGroup.add(User.Status.reject);
        statusGroup.add(User.Status.wait);

        return userRepository.countByClientUsersInStatus(statusGroup);
    }

    public int countByLiveWaitUsers(){
        List<User.Status> statusGroup = new ArrayList<>();
        statusGroup.add(User.Status.wait);

        return userRepository.countByClientUsersInStatus(statusGroup);
    }

    public int countByLiveRejectUsers(){
        List<User.Status> statusGroup = new ArrayList<>();
        statusGroup.add(User.Status.reject);

        return userRepository.countByClientUsersInStatus(statusGroup);
    }

    public int countByLiveAllowUsers(){
        List<User.Status> statusGroup = new ArrayList<>();
        statusGroup.add(User.Status.allow);

        return userRepository.countByClientUsersInStatus(statusGroup);
    }
}
