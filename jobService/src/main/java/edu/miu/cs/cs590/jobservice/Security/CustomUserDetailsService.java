package edu.miu.cs.cs590.jobservice.Security;

import edu.miu.cs.cs590.jobservice.Payload.Requests.UserAuth;
import javassist.NotFoundException;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final RestTemplate restTemplate;

    @Value("${app.AccountService}")
    private String accountServiceUrl;

    public CustomUserDetailsService(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }


    UserAuth getUserByToken(String jwtToken) throws NotFoundException {

        // create headers
        HttpHeaders headers = new HttpHeaders();
        // set custom header
        headers.set("Authorization", "Bearer "+jwtToken);

        // build the request
        HttpEntity request = new HttpEntity(headers);

        String url = accountServiceUrl+"/api/auth/verify-token";

        ResponseEntity<UserAuth> response =  this.restTemplate.exchange(url, HttpMethod.POST, request, UserAuth.class, 1);
        if(response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        }
        throw new NotFoundException("User with token: " + jwtToken + " not found");
    }

    @SneakyThrows
    @Override
    @Transactional
    public UserDetails loadUserByUsername(String jwtToken)
            throws UsernameNotFoundException {
       try{
           return UserPrincipal.create(getUserByToken(jwtToken));
       }catch (Exception e){
           throw  new UsernameNotFoundException("User not found with tokrn : " + jwtToken);
       }
    }

}