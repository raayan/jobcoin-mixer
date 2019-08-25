package com.raayanpillai.jobcoin.mixer.config;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinRequestException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import com.raayanpillai.jobcoin.mixer.util.JobcoinAddressGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Configuration
public class JobcoinConfiguration {

    @Value("${jobcoin.api.uri}")
    private String baseApi;

    @Value("${jobcoin.address.length}")
    private int addressLength;

    /**
     * @param webClientBuilder
     * @return a webClient configured to handle the responses the jobcoin api specifies
     */
    @Bean
    public WebClient webClient(@Autowired WebClient.Builder webClientBuilder) {
        return webClientBuilder
                .baseUrl(baseApi)
                .filter(ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
                    if (clientResponse.statusCode().isError()) {
                        if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                            return clientResponse.bodyToMono(ErrorDTO.class).flatMap(errorDetails ->
                                    Mono.error(new JobcoinTransactionException(errorDetails)));
                        }
                        return clientResponse.bodyToMono(ErrorDTO.class).flatMap(errorDetails ->
                                Mono.error(new JobcoinRequestException(errorDetails)));
                    }
                    return Mono.just(clientResponse);
                }))
                .build();
    }


    /**
     * Configuring the length of generated addresses
     *
     * @return an address generator
     */
    @Bean
    public AddressGenerator addressGenerator() {
        return new JobcoinAddressGenerator(addressLength);
    }
}
