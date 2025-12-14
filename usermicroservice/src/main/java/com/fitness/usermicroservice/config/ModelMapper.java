package com.fitness.usermicroservice.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@AllArgsConstructor
public class ModelMapper {
    @Bean
    public ModelMapper ModelMap(){
        return new ModelMapper();
    }
}
