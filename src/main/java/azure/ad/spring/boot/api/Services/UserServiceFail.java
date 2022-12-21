package azure.ad.spring.boot.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import azure.ad.spring.boot.api.Models.UserFail;
import azure.ad.spring.boot.api.Repository.UserRepoFail;


@Service
public class UserServiceFail {

    @Autowired
    private UserRepoFail usersrepofail;
    
    public UserFail insertar(UserFail user) {
        return usersrepofail.save(user);
    }

}
