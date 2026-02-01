package com.fitness.AiRecommendation;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@SpringBootApplication
public class AiRecommendationApplication {

	public static void main(String[] args) {
		SpringApplication.run(AiRecommendationApplication.class, args);
	}

}
