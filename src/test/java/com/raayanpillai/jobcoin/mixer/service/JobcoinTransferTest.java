package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.exception.MixTransferException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinExchange;
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

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class JobcoinTransferTest {

    private Address fromAddress;
    private Address toAddress;
    private Float amount;
    private Transaction transaction;

    @MockBean
    private JobcoinExchange jobcoinExchange;

    private JobcoinTransfer jobcoinTransfer;

    @Before
    public void setUp() throws Exception {
        jobcoinTransfer = new JobcoinTransfer(jobcoinExchange);
        fromAddress = new Address("fromAddress");
        toAddress = new Address("toAddress");
        amount = 100F;
        transaction = new Transaction(fromAddress, toAddress, amount);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void transact_sucess_true() {
        given(jobcoinExchange.submitTransaction(any(Transaction.class)))
                .willReturn(Mono.just(true));

        Boolean result = jobcoinTransfer.transact(transaction);

        assertTrue(result);
    }

    @Test
    public void transact_failed_false() {
        given(jobcoinExchange.submitTransaction(any(Transaction.class)))
                .willReturn(Mono.just(false));

        Boolean result = jobcoinTransfer.transact(transaction);

        assertFalse(result);
    }

    @Test
    public void getBalance() {
        given(jobcoinExchange.getBalance(any(Address.class)))
                .willReturn(Mono.just(amount));

        Float result = jobcoinTransfer.getBalance(fromAddress);

        assertEquals(amount, result);
    }

    @Test
    public void watchAndMove_sufficientFunds_moveFunds() {
        given(jobcoinExchange.getBalance(any(Address.class)))
                .willReturn(Mono.just(amount));

        given(jobcoinExchange.submitTransaction(any(Transaction.class)))
                .willReturn(Mono.just(true));

        Flux<Boolean> result = jobcoinTransfer.watchAndTransact(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext(true)
                .verifyComplete();
    }

    @Test
    public void watchAndMove_insufficientFunds_await() {
        given(jobcoinExchange.getBalance(any(Address.class)))
                .willReturn(Mono.just(0f));

        Flux<Boolean> result = jobcoinTransfer.watchAndTransact(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(2))
                .expectError(MixTransferException.class)
                .verify();
    }

    @Test
    public void watchAndMove_transferFailure_error() {
        given(jobcoinExchange.getBalance(any(Address.class)))
                .willReturn(Mono.just(amount));

        given(jobcoinExchange.submitTransaction(any(Transaction.class)))
                .willReturn(Mono.just(false));

        Flux<Boolean> result = jobcoinTransfer.watchAndTransact(transaction, Duration.ofSeconds(1), Duration.ofSeconds(3));

        StepVerifier.create(result)
                .expectSubscription()
                .expectNoEvent(Duration.ofSeconds(1))
                .expectNext(false)
                .expectComplete()
                .verify();
    }
}