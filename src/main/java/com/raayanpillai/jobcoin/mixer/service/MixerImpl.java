package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.exception.MixTransferException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

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

    /**
     * This is the main method of the mixer, it starts watching an address
     * and then triggers transactions based on what it sees
     *
     * @param mixRequest     the details of the mix (how much and where to put it)
     * @param depositAddress the address the user needs to place funds in (clean wallet)
     * @return a flux of whether or not the funds were received and transferred
     */
    public Flux<Boolean> monitorDeposit(MixRequest mixRequest, DepositAddress depositAddress) {
        // Compute duration between now and when the depositAddress expires
        Duration watchDuration = Duration.between(LocalDateTime.now(), depositAddress.getExpiryDate());
        Transaction transaction = new Transaction(depositAddress, houseAddress, mixRequest.getAmount());
        logger.info("Checking {} for {} seconds", depositAddress, watchDuration.getSeconds());
        return transfer.watchAndTransact(transaction, Duration.ofSeconds(1), watchDuration)
                .map(success -> executeMix(mixRequest))
                .onErrorReturn(MixTransferException.class, false);
    }

    /**
     * Improved the random algorithm to make each wallet have a minimum-bounded-chunk of your change
     * but also added a better random modifier
     */
    public Boolean executeMix(MixRequest mixRequest) {
        Random random = new Random();

        int deposits = mixRequest.getDestinations().size();
        float initialDivision = mixRequest.getAmount() / (deposits + 1);
        float[] randoms = new float[deposits];
        float sumRandoms = 0;
        for (int i = 0; i < deposits; i++) {
            randoms[i] = random.nextFloat();
            sumRandoms += randoms[i];
        }
        for (int i = 0; i < deposits; i++) {
            float randomModifier = randoms[i] * (initialDivision / sumRandoms);
            float minBoundedRandomChunk = initialDivision + randomModifier;
            Address destinationAddress = mixRequest.getDestinations().get(i);
            logger.info("Sending seeded piece {}, uniform random piece {}", minBoundedRandomChunk, minBoundedRandomChunk);
            executor.scheduleTransaction(new Transaction(houseAddress, destinationAddress, minBoundedRandomChunk));
        }

        return true;
    }
}
