package com.fitness.AiRecommendation.service;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Slf4j
@Service
public class GeminiService {
    private final WebClient webClient;

    @Value("${geminiCredentials.geminiUrl}")
    private String geminiUrl;
    @Value("${geminiCredentials.geminiAPIKey}")
    private String geminiAPIKey;

    public GeminiService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.build();
    }

    public String generateRecommendation(String details) {
        String response = "";
        try{
            Map<String,Object> requestBody = Map.of(
                    "contents", new Object[]{
                    Map.of(
                            "parts", new Object[]{
                                    Map.of("text", details)
                            }
                    )
                    }
            );

            response = webClient.post().
                    uri(geminiUrl)
                    .header("Content-Type","application/json")
                    .header("X-goog-api-key",geminiAPIKey)
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
            log.info("response: {} ",response);
            return response;
        }catch (Exception e){
            e.printStackTrace();
        }
        return response;
    }
}
