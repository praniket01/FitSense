package com.fitness.AiRecommendation.service;

import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.util.RecyclerPool;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fitness.AiRecommendation.model.Activity;
import com.fitness.AiRecommendation.model.Recommendation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiResponseProcessService {
    private final GeminiService geminiService;

    public Recommendation generateRecommendationResponse(Activity activity) {
        String prompt = createPromptforActivity(activity);
        String aiResponse = geminiService.generateRecommendation(prompt);
        log.info("AiResponse: {}", aiResponse);
        return processAiResponse(activity,aiResponse);

    }

    private Recommendation processAiResponse(Activity activity, String aiResponse) {
        try{
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(aiResponse);
            JsonNode textNode = jsonNode.path("candidates")
                    .get(0)
                    .path("content")
                    .path("parts")
                    .get(0)
                    .path("text");
            String jsonContent = textNode.asText()
                    .replaceAll("```json\\n","")
                    .replaceAll("\\n","")
                    .trim();
//                log.info("text: {}", text);

            //Convert jsonContent in readable format
            JsonNode analysisJson =  objectMapper.readTree(jsonContent);
            JsonNode analysisNode = analysisJson.path("analysis");
            StringBuilder fullAnalysis = new StringBuilder();
            addAnalysisSection(fullAnalysis,analysisNode,"overall","Overall:");
            addAnalysisSection(fullAnalysis,analysisNode,"pace","Pace:");
            addAnalysisSection(fullAnalysis,analysisNode,"heartRate","Heart Rate:");
            addAnalysisSection(fullAnalysis,analysisNode,"caloriesBurned","Calories Burned:");

            //Convert imporvements array to list
            List<String> imporvements = extractImprovements(analysisJson.path("improvements"));
            List<String> suggestions = extractSuggestions(analysisJson.path("suggestions"));
            List<String> Safety = extractSafety(analysisJson.path("safety"));

            return Recommendation.builder()
                    .activityId(activity.getId())
                    .userId(activity.getUserId())
                    .type(activity.getActivityType().toString())
                    .recommendation(fullAnalysis.toString().trim())
                    .improvements(imporvements)
                    .suggestions(suggestions)
                    .safety(Safety)
                    .createdAt(LocalDateTime.now())
                    .build();

        }catch (Exception e){
            e.printStackTrace();
            return createDefaultRecommendation(activity);
        }
    }

    private Recommendation createDefaultRecommendation(Activity activity) {
        return Recommendation.builder()
                .activityId(activity.getId())
                .userId(activity.getUserId())
                .type(activity.getActivityType().toString())
                .recommendation("Unable to generate recommendation")
                .improvements(Collections.singletonList("Unable to generate improvements"))
                .suggestions(Collections.singletonList("Unable to generate suggestions"))
                .safety(Collections.singletonList("Unable to generate safety"))
                .createdAt(LocalDateTime.now())
                .build();
    }

    private List<String> extractSafety(JsonNode analysisJson) {
        ArrayList<String> extractedSafety = new ArrayList<>();
        if(analysisJson.isArray()){
            analysisJson
                    .forEach(x -> extractedSafety.add(x.asText()));
        }
        return extractedSafety.isEmpty() ? Collections.singletonList("No data found") : extractedSafety;
    }

    private List<String> extractSuggestions(JsonNode analysisJson) {
        ArrayList<String> extractedSuggestions = new ArrayList<>();
        if(analysisJson.isArray()){

            analysisJson
                    .forEach(x -> {
                String workout = x.path("workout").asText();
                String description = x.path("workout").asText();
                extractedSuggestions.add(workout+" : "+description);
            });
        }
        return extractedSuggestions.isEmpty() ?  Collections.singletonList("No data found") : extractedSuggestions;
    }

    private List<String> extractImprovements(JsonNode analysisJson) {
        ArrayList<String> extractedImprovements = new ArrayList<>();
        if(analysisJson.isArray()){
            analysisJson.forEach(x -> {
                String areaofImprovement = x.path("area").asText();
                String recommendation = x.path("recommendation").asText();
                        extractedImprovements.add(areaofImprovement+": "+recommendation);
                    });

        }

        return extractedImprovements.isEmpty() ? Collections.singletonList("Unable to generate improvements"): extractedImprovements;
    }

    private void addAnalysisSection(StringBuilder fullAnalysis, JsonNode analysisNode, String key, String intent) {
        if(!analysisNode.path(key).isMissingNode()){
            fullAnalysis.append(intent)
                    .append(analysisNode.path(key).asText())
                    .append("\n\n");
        }

    }

    private String createPromptforActivity(Activity activity) {
        return String.format("""
        Analyze this fitness activity and provide detailed recommendations in the following EXACT JSON format:
        {
          "analysis": {
            "overall": "Overall analysis here",
            "pace": "Pace analysis here",
            "heartRate": "Heart rate analysis here",
            "caloriesBurned": "Calories analysis here"
          },
          "improvements": [
            {
              "area": "Area name",
              "recommendation": "Detailed recommendation"
            }
          ],
          "suggestions": [
            {
              "workout": "Workout name",
              "description": "Detailed workout description"
            }
          ],
          "safety": [
            "Safety point 1",
            "Safety point 2"
          ]
        }

        Analyze this activity:
        Activity Type: %s
        Duration: %d minutes
        Calories Burned: %d
        Additional Metrics: %s
        
        Provide detailed analysis focusing on performance, improvements, next workout suggestions, and safety guidelines.
        Ensure the response follows the EXACT JSON format shown above.
        """,
                activity.getActivityType(),
                activity.getDuration(),
                activity.getCaloriesBurned(),
                activity.getAdditionaMetrics()
        );
    }
}
