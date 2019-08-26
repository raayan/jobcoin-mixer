package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import reactor.core.publisher.Mono;

public interface Exchange {
    Mono<Float> getBalance(Address address);

    Mono<Boolean> submitTransaction(Transaction transaction);
}
