package azuread.spring.api.oauth2.Controllers;

import java.security.Principal;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;



import azuread.spring.api.oauth2.Models.User;
import azuread.spring.api.oauth2.Models.UserFail;
import azuread.spring.api.oauth2.Services.UserService;
import azuread.spring.api.oauth2.Services.UserServiceFail;
import net.minidev.json.JSONObject;
import reactor.core.publisher.Mono;

@Controller
@RestController
public class Controllers {


    @Value("${spring.cloud.azure.active-directory.profile.tenant-id}")
    private String tenant;
  
    @Value("${spring.cloud.azure.active-directory.credential.client-id}")
    private String client_id;
  
    @Value("${spring.cloud.azure.active-directory.credential.client-secret}")
    private String client_secret;

    @Autowired
    private UserService userservice;
    
    @Autowired
    private UserServiceFail userservicefail;
    

  @GetMapping("/principal")
  public Principal getPrincipal(Principal principal){

    return principal;
  }

  @PostMapping("/aad/users")
  public ResponseEntity<User> insertar(@RequestBody User user){
    userservice.insertar(user);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

  @PostMapping("/aad/usersfail")
  public ResponseEntity<UserFail> insertar(@RequestBody UserFail user){
    userservicefail.insertar(user);
    return ResponseEntity.status(HttpStatus.OK).body(user);
  }

    @PostMapping(path = "/aad/token", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<String> getTokens(@RequestBody String username, String password){
  
      String theUrl = "https://login.microsoftonline.com/"+tenant+"/oauth2/v2.0/token";
  
      if(username != null && password != null){
            try{
              HttpHeaders responHeaders = new HttpHeaders();
              responHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        
              MultiValueMap<String, String> restValues = new LinkedMultiValueMap<>();
        
              restValues.add("client_id",client_id);
              restValues.add("client_secret",client_secret);
              restValues.add("password", password);
              restValues.add("username", ((username.substring(0, username.indexOf("&")).substring(9))).replace("%40", "@"));
              restValues.add("grant_type","password");
              restValues.add("scope",(client_id+"/.default offline_access"));
        
              WebClient webClient = WebClient.create();
        
              return webClient
                      .post()
                      .uri(theUrl)
                      .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                      .bodyValue(restValues)
                      .retrieve()
                      .bodyToMono(String.class);
                    
              
              
            }
            catch (Exception eek) {
                System.out.println("** Exception: "+ eek.getMessage());
            }
          } else{

            try{

              RestTemplate response = new RestTemplate();
              HttpHeaders header = new HttpHeaders();

              String timestamp = ZonedDateTime.now(ZoneId.of("America/Santiago"))
              .format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));

              header.setContentType(MediaType.APPLICATION_JSON);

              JSONObject personJsonObject = new JSONObject();
              personJsonObject.put("email", (username.substring(0, username.indexOf("=")).replace("%40", "@")));
              personJsonObject.put("hour",timestamp);

              response.postForObject("http://localhost:8081/aad/usersfail", personJsonObject,String.class);

		          System.out.println("Response: " + response);
                    
            }
            catch (Exception eek) {
                System.out.println("** Exception: "+ eek.getMessage());
            }

          }
      return null;
  
    }
  
    @PostMapping(path = "/aad/refreshToken", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    public Mono<String> getRefreshToken(@RequestBody String refresh_token){
  
      String theUrl = "https://login.microsoftonline.com/"+tenant+"/oauth2/v2.0/token";
  
  
      try{
        HttpHeaders responHeaders = new HttpHeaders();
        responHeaders.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
  
        MultiValueMap<String, String> restValues = new LinkedMultiValueMap<>();
  
        restValues.add("client_id",client_id);
        restValues.add("scope",(client_id+"/.default offline_access"));
        restValues.add("client_secret",client_secret);
        restValues.add("refresh_token",(refresh_token.substring(14)));
        restValues.add("grant_type","refresh_token");
  
        WebClient webClient2 = WebClient.create();
  
        return webClient2
                .post()
                .uri(theUrl)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .bodyValue(restValues)
                .retrieve()
                .bodyToMono(String.class);
        
  
      }
      catch (Exception eek) {
          System.out.println("** Exception: "+ eek.getMessage());
      }
      return null;
  
    }


}
