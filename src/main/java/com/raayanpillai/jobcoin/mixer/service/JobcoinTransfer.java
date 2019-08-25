package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinAPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Optional;

@Service
public class JobcoinTransfer implements Transfer {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinTransfer.class);

    private final JobcoinAPI jobcoinAPI;

    public JobcoinTransfer(JobcoinAPI jobcoinAPI) {
        this.jobcoinAPI = jobcoinAPI;

        logger.info("Started");
    }

    /**
     * Uses the jobcoin api to make a transaction
     * in a thread blocking fashion
     *
     * @param fromAddress address to take from
     * @param toAddress   address to send to
     * @param amount      amount to send
     * @return true if the transaction completed, false otherwise
     */
    @Override
    public Boolean move(Address fromAddress, Address toAddress, Float amount) {
        Mono<ResponseDTO> responseDTOMono = jobcoinAPI.postTransaction(fromAddress, toAddress, amount);

        Optional<ResponseDTO> optionalResponseDTO = responseDTOMono
                .onErrorResume(JobcoinException.class, e -> Mono.empty())
                .blockOptional();

        return optionalResponseDTO.isPresent();
    }

    /**
     * @param address the address to check the balance of
     * @return the float value of the balance
     */
    @Override
    public Float getBalance(Address address) {
        Mono<AddressInfoDTO> addressInfoDTOMono = jobcoinAPI.getAddressInfo(address);

        AddressInfoDTO addressInfoDTO = addressInfoDTOMono.block();

        if (addressInfoDTO == null) {
            return Float.NaN;
        }

        return addressInfoDTO.getBalance();
    }

    /**
     * @param address address to check balance of
     * @return mono of the balance
     */
    @Override
    public Mono<Float> getMonoBalance(Address address) {
        return jobcoinAPI.getAddressInfo(address).map(AddressInfoDTO::getBalance);
    }

    /**
     * This is a non-blocking method that will watch a wallet for non-zero balance
     * and move its jobcoins to another wallet
     *
     * @param fromAddress      address to watch
     * @param toAddress        address to move to
     * @param watchAmount      amount to watch for
     * @param intervalDuration how often to check the wallet
     * @param watchDuration    the total time to check the wallet for
     */
    @Override
    public Flux<ResponseDTO> watchAndMove(Address fromAddress, Address toAddress, Float watchAmount,
                                          Duration intervalDuration, Duration watchDuration) {
        return Flux.interval(Duration.ofSeconds(1))
                .take(watchDuration)
                .flatMap(aLong -> jobcoinAPI.getAddressInfo(fromAddress))
                // Take until balance is sufficient
                .takeUntil(addressInfoDTO -> addressInfoDTO.getBalance() >= watchAmount)
                // Map a present balance
                .flatMap(addressInfoDTO -> {
                    if (addressInfoDTO.getBalance() >= watchAmount) {
                        logger.info("Balance Present {}", addressInfoDTO);
                        return jobcoinAPI
                                .postTransaction(fromAddress, toAddress, addressInfoDTO.getBalance())
                                .onErrorResume(JobcoinException.class, e -> Mono.empty());
                    }
                    logger.info("Balance {}", addressInfoDTO);
                    return Mono.empty();
                });
    }
}
