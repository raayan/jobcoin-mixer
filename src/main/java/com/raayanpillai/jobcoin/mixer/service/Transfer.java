package com.raayanpillai.jobcoin.mixer.service;


import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

/**
 * A component that handles moving currency from one account to another
 * and checking balances
 */
public interface Transfer {
    Boolean move(Address fromAddress, Address toAddress, Float amount);

    Float getBalance(Address address);

    Mono<Float> getMonoBalance(Address address);

    Flux<ResponseDTO> watchAndMove(Address fromAddress, Address toAddress, Float watchAmount,
                                   Duration intervalDuration, Duration watchDuration);
}
