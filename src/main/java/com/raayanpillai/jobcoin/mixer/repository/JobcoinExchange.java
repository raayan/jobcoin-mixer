package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinRequestException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

/**
 * The actual component that will submit transactions and check addresses
 * Using reactor the nuances of how a transaction fails are thrown away to return defaults
 */
@Repository
public class JobcoinExchange implements Exchange {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinExchange.class);

    private JobcoinAPI jobcoinAPI;

    public JobcoinExchange(JobcoinAPI jobcoinAPI) {
        this.jobcoinAPI = jobcoinAPI;
    }

    public Mono<Float> getBalance(Address address) {
        return jobcoinAPI.getAddressInfo(address)
                .map(AddressInfoDTO::getBalance);
    }


    public Mono<Boolean> submitTransaction(Transaction transaction) {
        return jobcoinAPI.postTransaction(transaction)
                .onErrorResume(JobcoinTransactionException.class, e -> Mono.empty())
                .onErrorResume(JobcoinRequestException.class, e -> Mono.empty())
                .map(responseDTO -> {
                    logger.info("{} {}", transaction, responseDTO);
                    return true;
                })
                .defaultIfEmpty(false);
    }
}
