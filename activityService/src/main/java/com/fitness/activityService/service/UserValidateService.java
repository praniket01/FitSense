package com.fitness.activityService.service;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientException;

@Service
@RequiredArgsConstructor
public class UserValidateService {

    private final WebClient userServiceWebClient;

//    @Bean
    public Boolean validateUser(String userId){
        try{
            return userServiceWebClient.get()
                    .uri("api/user/{userId}/validate", userId)
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();

        }catch (WebClientException e){
                e.printStackTrace();
        }
        return false;
    }
}
