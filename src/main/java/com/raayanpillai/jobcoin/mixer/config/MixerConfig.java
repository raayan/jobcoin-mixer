package com.raayanpillai.jobcoin.mixer.config;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinRequestException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import com.raayanpillai.jobcoin.mixer.util.JobcoinAddressGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

@Configuration
public class MixerConfig {

    private final String houseAddressString;
    private final int addressLength;

    public MixerConfig(@Value("${mixer.house.address}") String houseAddressString,
                       @Value("${mixer.address.length}") int addressLength) {
        this.houseAddressString = houseAddressString;
        this.addressLength = addressLength;
    }

    /**
     * @return an address generator with some length
     */
    @Bean
    public AddressGenerator addressGenerator() {
        return new JobcoinAddressGenerator(addressLength);
    }

    /**
     * @return the house address
     */
    @Bean
    public Address houseAddress() {
        return new Address(houseAddressString);
    }

    /**
     * @return the thread scheduler
     */
    @Bean
    public Scheduler scheduler() {
        return Schedulers.elastic();
    }

    @Bean
    public WebClient webClient(WebClient.Builder webClientBuilder, @Value("${jobcoin.api.uri}") String baseApi) {
        return webClientBuilder.baseUrl(baseApi)
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
}
