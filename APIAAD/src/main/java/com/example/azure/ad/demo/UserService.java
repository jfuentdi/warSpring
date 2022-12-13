package com.example.azure.ad.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    
    @Autowired
    private UserRepo usersrepo;

    public Users insertar(Users user) {
        return usersrepo.save(user);
    }
    public List<Users> listar() {
        return usersrepo.findAll();
    }

}