package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.exception.MixTransferException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.ArrayList;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyFloat;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class JobcoinTransferTest {

    private Address fromAddress;
    private Address toAddress;
    private Float amount;
    private Transaction transaction;

    @MockBean
    private JobcoinAPI jobcoinAPI;

    private JobcoinTransfer jobcoinTransfer;

    @Before
    public void setUp() throws Exception {
        jobcoinTransfer = new JobcoinTransfer(jobcoinAPI);
        fromAddress = new Address("fromAddress");
        toAddress = new Address("toAddress");
        amount = 100F;
        transaction = new Transaction(fromAddress, toAddress, amount);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void move() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.just(new ResponseDTO("OK")));

        Boolean result = jobcoinTransfer.move(transaction);

        assertTrue(result);
    }

    @Test
    public void move_transactionException() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.error(new JobcoinTransactionException(new ErrorDTO("INSUFFICIENT FUNDS"))));

        Boolean result = jobcoinTransfer.move(transaction);

        assertFalse(result);
    }

    @Test
    public void getBalance() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(amount, new ArrayList<>())));


        Float result = jobcoinTransfer.getBalance(fromAddress);

        assertEquals(amount, result);
    }

    @Test
    public void getMonoBalance() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(amount, new ArrayList<>())));

        Mono<Float> result = jobcoinTransfer.getMonoBalance(fromAddress);

        assertEquals(amount, result.block());
    }

    @Test
    public void watchAndMove_sufficientFunds_moveFunds() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(amount, new ArrayList<>())));

        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.just(new ResponseDTO("OK")));

        Flux<ResponseDTO> result = jobcoinTransfer.watchAndMove(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext(new ResponseDTO("OK"))
                .verifyComplete();
    }

    @Test
    public void watchAndMove_insufficientFunds_await() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(0f, new ArrayList<>())));

        Flux<ResponseDTO> result = jobcoinTransfer.watchAndMove(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(2))
                .expectError(MixTransferException.class)
                .verify();
    }

    @Test
    public void watchAndMove_transferFailure_error() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(amount, new ArrayList<>())));

        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.error(new JobcoinTransactionException(new ErrorDTO("INSUFFICIENT FUNDS"))));

        Flux<ResponseDTO> result = jobcoinTransfer.watchAndMove(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectError(JobcoinTransactionException.class)
                .verify();
    }
}