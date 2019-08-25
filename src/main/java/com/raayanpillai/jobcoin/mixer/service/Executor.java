package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;

/**
 * Executors schedule tasks to be completed in the future
 */
public interface Executor {
    void scheduleWithdrawal(Address toAddress, Float amount);
}
