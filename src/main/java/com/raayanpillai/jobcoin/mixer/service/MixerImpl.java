package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;

/**
 * The primary service in this application, it handles deciding how to mix
 * and executing those mixes
 */
@Service
public class MixerImpl implements Mixer {
    private static final Logger logger = LoggerFactory.getLogger(MixerImpl.class);
    private final Address houseAddress;
    private final Transfer transfer;
    private final Executor executor;
    private final AddressGenerator addressGenerator;
    private final long expiresInSeconds;

    public MixerImpl(Transfer transfer, Executor executor, AddressGenerator addressGenerator,
                     Address houseAddress, @Value("${mixer.expires.in}") long expiresInSeconds) {
        this.houseAddress = houseAddress;
        this.addressGenerator = addressGenerator;
        this.transfer = transfer;
        this.executor = executor;
        this.expiresInSeconds = expiresInSeconds;

        logger.info("Started with house address {} and defaultExpiresIn {}", houseAddress, expiresInSeconds);

        Float houseBalance = transfer.getBalance(houseAddress);

        logger.info("House Address Balance {}", houseBalance);
    }

    public DepositAddress createDepositAddress() {
        Address address = addressGenerator.generateAddress();

        return new DepositAddress(address.getAddress(), LocalDateTime.now().plusSeconds(expiresInSeconds));
    }

    public void monitorDeposit(MixRequest mixRequest, DepositAddress depositAddress) {
        // Compute duration between now and when the depositAddress expires
        Duration watchDuration = Duration.between(LocalDateTime.now(), depositAddress.getExpiryDate());
        Transaction transaction = new Transaction(houseAddress, depositAddress, mixRequest.getAmount());

        logger.info("Checking {} for {} seconds", depositAddress, watchDuration.getSeconds());
        transfer.watchAndMove(transaction, Duration.ofSeconds(1), watchDuration)
                .subscribe(responseDTO -> {
                    logger.info("Funds moved from {} to {}", depositAddress, houseAddress);
                    executeMix(mixRequest);
                }, e -> {
                    logger.error("Funds not received {}", depositAddress, e);
                });
    }

    public void executeMix(MixRequest mixRequest) {
        float mixBalance = mixRequest.getAmount();
        Random random = new Random();

        int counter = 0;
        for (Address address : mixRequest.getDestinationAddresses()) {
            counter++;
            float withdrawAmount = (counter < mixRequest.getDestinationAddresses().size()) ?
                    mixBalance * random.nextFloat() : mixBalance;

            mixBalance -= withdrawAmount;

            executor.scheduleWithdrawal(address, withdrawAmount);
        }

        logger.info("Mix balance for {} is {}", mixRequest, mixBalance);
    }
}
