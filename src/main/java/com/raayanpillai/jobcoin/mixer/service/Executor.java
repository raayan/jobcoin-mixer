package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Transaction;

/**
 * Executors schedule tasks to be completed in the future
 */
public interface Executor {
    void scheduleTransaction(Transaction transaction);
}
