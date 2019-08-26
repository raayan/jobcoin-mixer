package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.MixTransferException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinExchange;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class JobcoinTransfer implements Transfer {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinTransfer.class);

    private final JobcoinExchange jobcoinExchange;

    public JobcoinTransfer(JobcoinExchange jobcoinExchange) {
        this.jobcoinExchange = jobcoinExchange;

        logger.info("Started");
    }

    /**
     * Uses the jobcoin api to make a transaction
     * in a thread blocking fashion
     *
     * @param transaction the transaction to transact
     * @return true if the transaction completed, false otherwise
     */
    @Override
    public Boolean transact(Transaction transaction) {
        return jobcoinExchange.submitTransaction(transaction).block();
    }

    /**
     * @param address the address to check the balance of
     * @return the float value of the balance
     */
    @Override
    public Float getBalance(Address address) {
        return jobcoinExchange.getBalance(address).block();
    }

    /**
     * This is a non-blocking method that will watch a wallet for non-zero balance
     * and transact its jobcoins to another wallet, if push an error
     *
     * @param transaction the transaction to execute after watching
     * @param intervalDuration how often to check the wallet
     * @param watchDuration    the total time to check the wallet for
     */
    @Override
    public Flux<Boolean> watchAndTransact(Transaction transaction, Duration intervalDuration,
                                          Duration watchDuration) {
        return Flux.interval(intervalDuration)
                .take(watchDuration)
                .flatMap(aLong -> jobcoinExchange.getBalance(transaction.getFromAddress()))
                // Take until balance is sufficient
                .takeUntil(balance -> balance >= transaction.getAmount())
                // Map a present balance
                .flatMap(balance -> {
                    if (balance >= transaction.getAmount()) {
                        logger.info("Balance Present {}", balance);
                        return jobcoinExchange.submitTransaction(transaction);
                    }
                    return Mono.empty();
                })
                .switchIfEmpty(s -> s.onError(new MixTransferException(new ErrorDTO("Balance insufficient"))));
    }
}
