package com.raayanpillai.jobcoin.mixer.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class JobcoinConfiguration {

    @Value("${jobcoin.api.uri}")
    private String jobcoinApi;

    @Bean
    public WebClient webClient(@Autowired WebClient.Builder webClientBuilder) {
        return webClientBuilder.baseUrl(jobcoinApi).build();
    }
}
