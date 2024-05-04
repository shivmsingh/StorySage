package com.major.recommendation.Configuration;



import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl("https://434f6464-61ba-46e2-b816-8d3381329a86.mock.pstmn.io")
                .build();
    }
}
