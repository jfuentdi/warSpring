package azuread.spring.api.oauth2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import azuread.spring.api.oauth2.Models.UserFail;
import azuread.spring.api.oauth2.Repository.UserRepoFail;


@Service
public class UserServiceFail {

    @Autowired
    private UserRepoFail usersrepofail;
    
    public UserFail insertar(UserFail user) {
        return usersrepofail.save(user);
    }

}
