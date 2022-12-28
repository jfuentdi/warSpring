package azuread.spring.api.oauth2.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import azuread.spring.api.oauth2.Models.UserFail;


@Repository
public interface UserRepoFail extends JpaRepository<UserFail, Integer>{
    
}
