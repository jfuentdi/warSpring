package azuread.spring.api.oauth2.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import azuread.spring.api.oauth2.Models.User;
import azuread.spring.api.oauth2.Repository.UserRepo;


@Service
public class UserService {

    @Autowired
    private UserRepo usersrepo;
    
    public User insertar(User user) {
        return usersrepo.save(user);
    }

}
