package com.fitness.activityService.DTO;

import com.fitness.activityService.model.ActivityType;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class ActivityResponse {
    private  String id;
    private String userId;
    private ActivityType activityType;
    private Integer duration;
    private  Integer caloriesBurned;
    private LocalDateTime startTime;
    private Map<String,String> additionaMetrics;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
