package com.fitness.activityService.service;

import com.fitness.activityService.DTO.ActivityRequest;
import com.fitness.activityService.DTO.ActivityResponse;
import com.fitness.activityService.Entity.Activity;
import com.fitness.activityService.Repository.ActivityRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class ActivityService {
    private final ActivityRepository activityRepository;
    private final UserValidateService userValidateService;
    private final KafkaTemplate<String , Activity> kafkaTemplate;

    @Value("${kafka.topic.name}")
    private String topicName;

    public ActivityResponse trackActivity(@RequestBody ActivityRequest activityRequest){

        Boolean result = userValidateService.validateUser(activityRequest.getId());

        if(!result){
            throw  new RuntimeException("Invalid USer "+activityRequest.getUserId());
        }

        Activity activity = convertActivityRequestToEntity(activityRequest);
        Activity createdActivity =  activityRepository.save(activity);
        try{
            kafkaTemplate.send(topicName, createdActivity.getUserId(), createdActivity);
        }catch (Exception e){
            e.printStackTrace();
        }

        return convertToActivityResponse(createdActivity);
    }

    public Activity convertActivityRequestToEntity(ActivityRequest activityRequest){
        Activity activity = Activity.builder()
                .id(activityRequest.getId()).
                userId(activityRequest.getUserId()).
                caloriesBurned(activityRequest.getCaloriesBurned()).
                duration(activityRequest.getDuration()).
                startTime(activityRequest.getStartTime()).
                activityType(activityRequest.getActivityType()).build();

        return activity;
    }

    public ActivityResponse convertToActivityResponse(Activity createdActivity){
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(createdActivity.getId());
        activityResponse.setUserId(createdActivity.getUserId());
        activityResponse.setActivityType(createdActivity.getActivityType());
        activityResponse.setDuration(createdActivity.getDuration());
        activityResponse.setStartTime(createdActivity.getStartTime());
        activityResponse.setCaloriesBurned(createdActivity.getCaloriesBurned());
        activityResponse.setCreatedAt(createdActivity.getCreatedAt());
        activityResponse.setUpdatedAt(createdActivity.getUpdatedAt());
        activityResponse.setAdditionaMetrics(createdActivity.getAdditionaMetrics());
        return activityResponse;
    }
}
