package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * This is a wrapper around the provided Jobcoin API, this service could be
 * switched out for something that interfaces with other transaction networks (cryptocurrencies...)
 */
@Repository
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
        Mono<AddressInfoDTO> addressInfoDTOMono = webClient
                .get()
                .uri("/addresses/{address}", address.getAddress())
                .retrieve()
                .bodyToMono(AddressInfoDTO.class);

        return addressInfoDTOMono;
    }


    /**
     * @return a list of all the transactions on the Jobcoin api essentially the public ledger
     */
    public Mono<TransactionDTO[]> getTransactions() {
        Mono<TransactionDTO[]> transactionListMono = webClient
                .get()
                .uri("/transactions")
                .retrieve()
                .bodyToMono(TransactionDTO[].class);

        return transactionListMono;
    }

    /**
     * @param fromAddress the address to withdraw from
     * @param toAddress   the address to deposit into
     * @param amount      the amount to move
     * @return information regarding the transaction
     */
    public Mono<ResponseDTO> postTransaction(Address fromAddress, Address toAddress, Float amount) {
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("fromAddress", fromAddress.getAddress());
        parameters.add("toAddress", toAddress.getAddress());
        parameters.add("amount", String.valueOf(amount));

        Mono<ResponseDTO> responseDTOMono = webClient
                .post()
                .uri("/transactions")
                .syncBody(parameters)
                .retrieve()
                .bodyToMono(ResponseDTO.class);

        return responseDTOMono;
    }
}
/*

 */