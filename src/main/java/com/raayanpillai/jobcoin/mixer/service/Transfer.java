package com.raayanpillai.jobcoin.mixer.service;


import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * A component that handles moving currency from one account to another
 * and checking balances
 */
public interface Transfer {
    Boolean transact(Transaction transaction);

    Float getBalance(Address address);

    Flux<Boolean> watchAndTransact(Transaction transaction, Duration intervalDuration, Duration watchDuration);
}
