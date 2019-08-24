package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinApiException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class JobcoinApi {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinApi.class);

    private WebClient webClient;

    @Autowired
    public JobcoinApi(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * @param address The address you want information about
     * @return an addressDTO whether it exists or not
     */
    public Mono<AddressInfoDTO> getAddressInfo(Address address) {
        Mono<AddressInfoDTO> addressInfoMono = webClient
                .get()
                .uri("/addresses/{address}", address.getAddress())
                .retrieve()
                .bodyToMono(AddressInfoDTO.class);

        return addressInfoMono;
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
     * @param amount      the amount to transfer
     * @return information regarding the transaction
     */
    public Mono<ResponseDTO> postTransaction(Address fromAddress, Address toAddress, Float amount) {
        MultiValueMap<String, Object> parameters = new LinkedMultiValueMap<>();
        parameters.add("fromAddress", fromAddress.getAddress());
        parameters.add("toAddress", toAddress.getAddress());
        parameters.add("amount", amount);

        Mono<ResponseDTO> responseMono = webClient
                .post()
                .uri("/transactions")
                .syncBody(parameters)
                .retrieve()
                .onStatus(HttpStatus::is4xxClientError, clientResponse -> {
                    if (clientResponse.statusCode() == HttpStatus.UNPROCESSABLE_ENTITY) {
                        return Mono.error(new JobcoinTransactionException("Transaction execution error, likely insufficient funds"));
                    }
                    return Mono.error(new JobcoinApiException("Request error, likely bad parameters"));
                })
                .bodyToMono(ResponseDTO.class);

        return responseMono;
    }
}
