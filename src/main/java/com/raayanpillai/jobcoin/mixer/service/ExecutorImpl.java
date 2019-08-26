package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.scheduler.Scheduler;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Service
public class ExecutorImpl implements Executor {
    private static final Logger logger = LoggerFactory.getLogger(ExecutorImpl.class);

    private final Scheduler scheduler;
    private final Transfer transfer;
    private final Random random;
    private final long minDelay;
    private final long maxDelay;

    public ExecutorImpl(Transfer transfer, Scheduler scheduler,
                        @Value("${mixer.delay.min}") long minDelay, @Value("${mixer.delay.max}") long maxDelay) {
        this.transfer = transfer;
        this.scheduler = scheduler;
        this.random = new Random();
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    /**
     * Schedules a transaction to be executed in the future
     *
     * @param transaction
     */
    @Override
    public void scheduleTransaction(Transaction transaction) {
        // Some random delay
        long delay = minDelay + (long) (random.nextFloat() * (maxDelay - minDelay));

        Scheduler.Worker worker = scheduler.createWorker();

        logger.info("Scheduling {} in {} second(s)", transaction, delay);
        worker.schedule(() -> {
            logger.info("Executing {}", transaction);
            transfer.transact(transaction);
        }, delay, TimeUnit.SECONDS);
    }
}
