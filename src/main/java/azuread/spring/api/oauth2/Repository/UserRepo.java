package azuread.spring.api.oauth2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import azuread.spring.api.oauth2.Models.User;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
    
}
