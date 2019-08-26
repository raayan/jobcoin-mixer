package com.raayanpillai.jobcoin.mixer.service;


import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * A component that handles moving currency from one account to another
 * and checking balances
 */
public interface Transfer {
    Boolean move(Transaction transaction);

    Float getBalance(Address address);

    Mono<Float> getMonoBalance(Address address);

    Flux<ResponseDTO> watchAndMove(Transaction transaction, Duration intervalDuration, Duration watchDuration);
}
