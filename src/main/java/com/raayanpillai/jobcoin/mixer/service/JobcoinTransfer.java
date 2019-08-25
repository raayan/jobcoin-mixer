package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinException;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinAPI;
import com.raayanpillai.jobcoin.mixer.model.Address;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Service
public class JobcoinTransfer implements Transfer {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinTransfer.class);

    private JobcoinAPI jobcoinAPI;

    public JobcoinTransfer(JobcoinAPI jobcoinAPI) {
        this.jobcoinAPI = jobcoinAPI;
    }

    /**
     * Uses the jobcoin api to make a transaction
     * Not blocking this method could allow for a performance increase later on
     *
     * @param fromAddress address to take from
     * @param toAddress   address to send to
     * @param amount      amount to send
     * @return true if the transaction completed, false otherwise
     */
    @Override
    public Boolean transfer(Address fromAddress, Address toAddress, Float amount) {
        Mono<ResponseDTO> responseDTOMono = jobcoinAPI.postTransaction(fromAddress, toAddress, amount);

        Optional<ResponseDTO> optionalResponseDTO = responseDTOMono
                .onErrorResume(JobcoinException.class, e -> Mono.empty())
                .blockOptional();

        return optionalResponseDTO.isPresent();
    }

    @Override
    public Float getBalance(Address address) {
        Mono<AddressInfoDTO> addressInfoDTOMono = jobcoinAPI.getAddressInfo(address);

        AddressInfoDTO addressInfoDTO = addressInfoDTOMono.block();

        if (addressInfoDTO == null) {
            return null;
        }

        return addressInfoDTO.getBalance();
    }
}
