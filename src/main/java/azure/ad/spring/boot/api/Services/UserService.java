package azure.ad.spring.boot.api.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import azure.ad.spring.boot.api.Models.User;
import azure.ad.spring.boot.api.Repository.UserRepo;


@Service
public class UserService {

    @Autowired
    private UserRepo usersrepo;
    
    public User insertar(User user) {
        return usersrepo.save(user);
    }

}
