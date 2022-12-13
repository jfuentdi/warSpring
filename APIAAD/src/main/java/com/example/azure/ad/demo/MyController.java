package com.example.azure.ad.demo;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;





@CrossOrigin
@RestController()
public class MyController {

    private static final Logger log = LoggerFactory.getLogger(AzureAdJwtAuthenticationTokenFilter.class);

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";

    //info properties
    @Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenant;

    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    private String clientid;

    //controllers
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/refreshToken")
    @ResponseBody
    public void token(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String tmp = request.getHeader(TOKEN_HEADER);
        if (tmp != null) {
            if (tmp.startsWith(TOKEN_TYPE)) {
                String jwtText = tmp.substring(TOKEN_TYPE.length());
                if (log.isDebugEnabled()) {
                    log.debug("Raw JWT token: >>{}<<", jwtText);

                }
                AzureAdJwtToken jwt = new AzureAdJwtToken(jwtText);

                response.setContentType("application/json");
                String json =  "{\"Refresh Token\": \""+jwt.getToken()+"\"}";
                PrintWriter out = response.getWriter();
                out.write(json);

            }
        } 


    }

    
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/token")
    public Map<String, Object> token() {
        Map<String, Object> response = new HashMap<>();
        response.put("content","I have user permissions");
        return response;
    } 

    @RestController
    @RequestMapping("/usuarios")
    public class UsersBD {

        @Autowired
        private UserService userservice;

        @GetMapping
        public List<Users> listar(){
            return userservice.listar();
        }

        @PostMapping()
        public ResponseEntity<Users> insertar(@RequestBody Users user) {
            userservice.insertar(user);
            return ResponseEntity.status(HttpStatus.OK).body(user);
        }



    }

    @GetMapping({"/index2", "/"})
    public ModelAndView index2(){
        var result = new ModelAndView();
        result.addObject("message", tenant);
        result.addObject("message2", clientid);
        result.setViewName("index2");
        return result;
    }

    


}

    







    
