package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Collections.EMPTY_LIST;

/**
 * This is a wrapper around the provided Jobcoin API, this service could be
 * switched out for something that interfaces with other transaction networks (cryptocurrencies...)
 */
@Component
public class JobcoinAPI {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinAPI.class);

    private final WebClient webClient;

    public JobcoinAPI(WebClient webClient) {
        this.webClient = webClient;

        logger.info("Started");
    }

    /**
     * @param address The address you want information about
     * @return details about an address
     */
    public Mono<AddressInfoDTO> getAddressInfo(Address address) {
        return webClient
                .get()
                .uri("/addresses/{address}", address.getAddress())
                .retrieve()
                .bodyToMono(AddressInfoDTO.class)
                .onErrorReturn(new AddressInfoDTO(0f, EMPTY_LIST));
    }

    /**
     * @return a list of all the transactions on the Jobcoin api essentially the public ledger
     */
    public Flux<TransactionDTO> getTransactions() {
        return webClient
                .get()
                .uri("/transactions")
                .retrieve()
                .bodyToFlux(TransactionDTO.class)
                .onErrorResume(Exception.class, e -> Flux.empty());
    }

    /**
     * @param transaction
     * @return information regarding the transaction
     */
    public Mono<ResponseDTO> postTransaction(Transaction transaction) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("fromAddress", transaction.getFromAddress().getAddress());
        parameters.add("toAddress", transaction.getToAddress().getAddress());
        parameters.add("amount", String.valueOf(transaction.getAmount()));

        return webClient
                .post()
                .uri("/transactions")
                .syncBody(parameters)
                .retrieve()
                .bodyToMono(ResponseDTO.class);
    }
}