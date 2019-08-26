package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;
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
    private final Address houseAddress;
    private final Random random;
    private final long minDelay;
    private final long maxDelay;

    public ExecutorImpl(Transfer transfer, Scheduler scheduler, Address houseAddress,
                        @Value("${mixer.delay.min}") long minDelay, @Value("${mixer.delay.max}") long maxDelay) {
        this.transfer = transfer;
        this.scheduler = scheduler;
        this.houseAddress = houseAddress;
        this.random = new Random();
        this.minDelay = minDelay;
        this.maxDelay = maxDelay;
    }

    @Override
    public void scheduleWithdrawal(Address toAddress, Float amount) {
        // Some random delay
        long delay = minDelay + (long) (random.nextFloat() * (maxDelay - minDelay));

        Scheduler.Worker worker = scheduler.createWorker();

        logger.info("Scheduling Transfer {} from house to {} in {} second(s)", amount, toAddress.getAddress(), delay);
        worker.schedule(() -> {
            logger.info("Transferring {} from house to {}", amount, toAddress.getAddress());
            transfer.move(new Transaction(houseAddress, toAddress, amount));
        }, delay, TimeUnit.SECONDS);
    }
}
