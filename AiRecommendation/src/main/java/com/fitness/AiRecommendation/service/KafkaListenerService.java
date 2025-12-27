package com.fitness.AiRecommendation.service;

import com.fitness.AiRecommendation.model.Activity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaListenerService {
    @KafkaListener(topics = "${kafka.topic.name}" , groupId = "${spring.kafka.consumer.group-id}")
    public void onMessage(Activity activity) {
        log.info("Received Activity: {}", activity.getId());
        }
}
