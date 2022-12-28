package azuread.spring.api.oauth2.Security;

import java.io.IOException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


/**
 * 
 * @author Jfuentdi
 *
 */
@Service
public class AzureAdJwtAuthenticationTokenFilter  extends OncePerRequestFilter{

    private static final Logger log = LoggerFactory.getLogger(AzureAdJwtAuthenticationTokenFilter.class);

    private static final String TOKEN_HEADER = "Authorization";
    private static final String TOKEN_TYPE = "Bearer ";

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException{

      
    }

    @Override
    protected void doFilterInternal(jakarta.servlet.http.HttpServletRequest request,
            jakarta.servlet.http.HttpServletResponse response, jakarta.servlet.FilterChain filterChain)
            throws jakarta.servlet.ServletException, IOException {
                // Retrieve authorization token from header.
                String tmp = request.getHeader(TOKEN_HEADER);

                if (tmp != null) {
                    if (tmp.startsWith(TOKEN_TYPE)) {
                        String jwtText = tmp.substring(TOKEN_TYPE.length());
                        if (log.isDebugEnabled()) {
                            log.debug("Raw JWT token: >>{}<<", jwtText);
                        }

                        
        
                        try {
                            AzureAdJwtToken jwt = new AzureAdJwtToken(jwtText);
                            // Verify Azure jwt token.

                            if (log.isDebugEnabled()) {
                                log.debug("JWT: {}", jwt);
                            }
                            jwt.verify();
                            if (log.isDebugEnabled()) {
        
                                log.debug("Token verification success!");

                            }
        
                            // If token verification success, create Spring authentication object and grant authorities.
                            List<GrantedAuthority> authorities = new ArrayList<>();
        
                            // set roles
                            if ("user@example.com".equals(jwt.getUniqueName())) {
                                authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
                            } else {
                                authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
                            } 
        
                            // set authentication to Spring so Spring Security understands it.
                            Authentication authentication = new PreAuthenticatedAuthenticationToken(jwt, null, authorities);
                            authentication.setAuthenticated(true);
                            log.info("Request token verification success. {}", authentication);
                            SecurityContextHolder.getContext().setAuthentication(authentication);

                            OkHttpClient client = new OkHttpClient().newBuilder()
                            .build();
                            MediaType mediaType = MediaType.parse("application/json");
                            RequestBody body = RequestBody.create("{\r\n    \"ipAddr\":\""+jwt.ipAddr+"\",\r\n    \"name\":\""+jwt.name+"\",\r\n    \"uniqueName\":\""+jwt.uniqueName+"\"\r\n}",mediaType);
                            Request requests = new Request.Builder()
                            .url("http://localhost:8081/aad/users")
                            .method("POST", body)
                            .addHeader("Content-Type", "application/json")
                            .build();
                            Response responses = client.newCall(requests).execute();
                            System.out.println(responses);
                            
                        } catch (CertificateException e) {
                            throw new RuntimeException(e);
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }
                } else {
                    if (log.isDebugEnabled()) {

                        log.debug("This request does not contain any authorization token.");
                    }
                }
        
                filterChain.doFilter(request, response);
        
    }


}
