package com.major.bookcatalog.RecommendationUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class KafkaProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public KafkaProducer(KafkaTemplate<String, String> kafkaTemplate, ObjectMapper objectMapper) {
        this.kafkaTemplate = kafkaTemplate;
        this.objectMapper = objectMapper;
    }

    public void publishRecommendation(Payload payload) {
        try {
            String jsonString = objectMapper.writeValueAsString(payload);
            kafkaTemplate.send("Recommendation", jsonString);
            log.info("Published recommendation: {}", jsonString);
        } catch (Exception e) {
            log.error("Failed to publish recommendation", e);
        }
    }
}
