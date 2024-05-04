package com.major.recommendation.Kafka;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.major.recommendation.DTO.Payload;
import com.major.recommendation.Service.RecommendationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class KafkaConsumer {
    private final ObjectMapper objectMapper;
    private final RecommendationService recommendationService;


    public KafkaConsumer(ObjectMapper objectMapper, RecommendationService recommendationService) {
        this.objectMapper = objectMapper;
        this.recommendationService = recommendationService;
    }

    @KafkaListener(topics = "Recommendation", groupId = "recommendation-group")
    public void consume(String message) {
        try {
            Payload payload = objectMapper.readValue(message, Payload.class);
            log.info("Received recommendation: {}", payload);
            if(payload.getMethod() == "SAVE"){

                // convert the payload to a JSON string
                String jsonPayload = objectMapper.writeValueAsString(payload);

                // call the getRecommendations method with the JSON payload
                Mono<String> recommendations = recommendationService.addRecommendations(Mono.just(jsonPayload));

                // process the recommendations
                recommendations.subscribe(result -> {
                    log.info("Received recommendations: {}", result);
                    // do something with the recommendations
                });
            }else {
                recommendationService.deleteRecommendation(payload.getUsername(), payload.getBookId());
                log.info("Deleted Recommendation for Username {} and Book {}", payload.getUsername(), payload.getBookId());
            }

            // process the recommendation payload
        } catch (Exception e) {
            log.error("Failed to process recommendation", e);
        }
    }
}
