package com.major.recommendation.Service;

import com.major.recommendation.Modal.Recommendation;
import com.major.recommendation.Repository.RecommendationRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
@Slf4j
public class RecommendationService {


    private final WebClient webClient;
    private final RecommendationRepository recommendationRepository;

    @Autowired
    public RecommendationService(WebClient webClient, RecommendationRepository recommendationRepository) {
        this.webClient = webClient;
        this.recommendationRepository = recommendationRepository;
    }

    public List<Recommendation> getAllRecommendations(String username, Long book_id, int page, int size){
        Pageable pageable = PageRequest.of(page, size);
        return recommendationRepository.findByUsernameAndBookId(username,book_id,pageable);
    }

    public Mono<String> addRecommendations(Mono<String> payload) {
        return webClient.post()
                .uri("/recommendations")
                .body(payload, String.class)
                .retrieve()
                .bodyToMono(String.class)
                .onErrorResume(WebClientResponseException.class, e -> Mono.empty());
    }

    @Transactional
    public void deleteRecommendation(String username, Long book_id){
        recommendationRepository.deleteByUsernameAndBookId(username,book_id);
    }

}
