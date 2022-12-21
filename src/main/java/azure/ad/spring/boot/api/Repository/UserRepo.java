package azure.ad.spring.boot.api.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import azure.ad.spring.boot.api.Models.User;


@Repository
public interface UserRepo extends JpaRepository<User, Integer>{
    
}
