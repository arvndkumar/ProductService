package com.ecommerce.productservice.util;

import com.ecommerce.productservice.exception.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;

@Component
@RequiredArgsConstructor
public class ApplicationCommonsImpl implements ApplicationCommons {


    private final RestTemplate restTemplate;

    @Value("${userservice.base-url:http://UserService}")
    private String userServiceBaseUrl;


    @Override
    public void validateToken(String bearer)
    {
        if(bearer == null || bearer.isBlank() || !bearer.startsWith("Bearer "))
        {
            throw new InvalidTokenException("Invalid token");
        }

        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.AUTHORIZATION, bearer);
        HttpEntity<Void> entity = new HttpEntity<>(headers);

        try{
            restTemplate.exchange(
                    userServiceBaseUrl + "/auth/me",
                    HttpMethod.GET,
                    entity,
                    String.class);
        } catch (HttpStatusCodeException e){
            if(e.getStatusCode() == HttpStatus.UNAUTHORIZED || e.getStatusCode() == HttpStatus.FORBIDDEN)
            {
                throw new InvalidTokenException("Unauthorized: Invalid or expired token");
            }
            throw e;
        }
    }
}
