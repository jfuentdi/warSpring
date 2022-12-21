package azure.ad.spring.boot.api.Security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true, prePostEnabled = true) // enabling the authorization check before each service call.
public class Config  {

    @Autowired
    private  AzureAdJwtAuthenticationTokenFilter filter;

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // allow access if we stored our front-end pages in the same project.
        // if the static pages are hosted somewhere else, ignore this line.
        http.authorizeHttpRequests().requestMatchers("/aad/**").permitAll();

        // we only host RESTful API and every services are protected.
        http.authorizeHttpRequests().anyRequest().authenticated();

        // we are using token based authentication. csrf is not required.
        http.csrf().disable();

        http.addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class);

        // the following code handle preflight messages so ajax calls will work...

        // prepare cors config
        CorsConfiguration corsConfig = new CorsConfiguration();
        
        // Access-Control-Allow-Origin: *
        corsConfig.addAllowedOrigin("*");
        
        // Access-Control-Allow-Methods: GET, POST, PUT, DELETE, OPTIONS
        corsConfig.addAllowedMethod(HttpMethod.GET);
        corsConfig.addAllowedMethod(HttpMethod.POST);
        
        // Access-Control-Max-Age: 3600
        corsConfig.setMaxAge(60L); 
        
        // Access-Control-Allow-Headers: authorization, content-type, xsrf-token
        corsConfig.addAllowedHeader("authorization");
        corsConfig.addAllowedHeader("content-type");
        corsConfig.addAllowedHeader("xsrf-token");
        
        // Access-Control-Expose-Headers: xsrf-token
        corsConfig.addExposedHeader("xsrf-token");
        
        // ant match any request path to apply this policy.
        UrlBasedCorsConfigurationSource corsConfigSource = new UrlBasedCorsConfigurationSource();
        corsConfigSource.registerCorsConfiguration("/**", corsConfig); 

        // this is required to handle preflight message...
        http.addFilterBefore(new CorsFilter(corsConfigSource), ChannelProcessingFilter.class);

        return http.build();
        
    }


}
