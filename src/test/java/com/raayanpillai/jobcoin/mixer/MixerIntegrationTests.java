package com.raayanpillai.jobcoin.mixer;

import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinExchange;
import com.raayanpillai.jobcoin.mixer.service.*;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;
import reactor.test.scheduler.VirtualTimeScheduler;

import java.time.Duration;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class MixerIntegrationTests {
    @MockBean
    private JobcoinExchange jobcoinExchange;

    @MockBean
    private AddressGenerator addressGenerator;

    private VirtualTimeScheduler virtualTimeScheduler;
    private Transfer transfer;
    private Executor executor;
    private Mixer mixer;

    private Address depositAddress;
    private Address houseAddress;
    private float houseBalance;
    private long minDelay;
    private long maxDelay;
    private long expiresIn;

    @Before
    public void setUp() {
        virtualTimeScheduler = VirtualTimeScheduler.create();
        houseAddress = new Address("IntegrationTest_House");
        depositAddress = new Address("IntegrationTest_Deposit");
        houseBalance = 10f;
        minDelay = 10;
        maxDelay = 20;
        expiresIn = 5;


        given(jobcoinExchange.getBalance(houseAddress)).willReturn(Mono.just(houseBalance));
        given(addressGenerator.generateAddress()).willReturn(depositAddress);

        transfer = new JobcoinTransfer(jobcoinExchange);
        executor = new ExecutorImpl(transfer, virtualTimeScheduler, minDelay, maxDelay);
        mixer = new MixerImpl(transfer, executor, addressGenerator, houseAddress, expiresIn);
    }

    @Test
    public void integrationTest_fundsReceived_mixSucceeds() {
        MixRequest mixRequest = new MixRequest(10f, Arrays.asList(
                new Address("IntegrationTest_Destination1"),
                new Address("IntegrationTest_Destination2"),
                new Address("IntegrationTest_Destination3")));

        given(jobcoinExchange.getBalance(any(Address.class))).willReturn(Mono.just(10f));
        given(jobcoinExchange.submitTransaction(any(Transaction.class))).willReturn(Mono.just(true));

        DepositAddress depositAddress = mixer.createDepositAddress();

        // Start the mix
        mixer.monitorDeposit(mixRequest, depositAddress).blockLast();

        // Skip ahead in time to after the executors tasks completed
        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(maxDelay));

        // Verify that the exchange transacted 4 times once to the house, thrice from the house
        then(jobcoinExchange).should(times(4)).submitTransaction(any(Transaction.class));
    }

    @Test
    public void integrationTest_fundsNotReceived_mixAbandoned() {
        MixRequest mixRequest = new MixRequest(10f, Arrays.asList(
                new Address("IntegrationTest_Destination1"),
                new Address("IntegrationTest_Destination2"),
                new Address("IntegrationTest_Destination3")));

        given(jobcoinExchange.getBalance(any(Address.class))).willReturn(Mono.just(0f));
        given(jobcoinExchange.submitTransaction(any(Transaction.class))).willReturn(Mono.just(true));

        DepositAddress depositAddress = mixer.createDepositAddress();

        mixer.monitorDeposit(mixRequest, depositAddress).blockLast();

        virtualTimeScheduler.advanceTimeBy(Duration.ofSeconds(maxDelay));

        // Verify that the exchange never transacted
        then(jobcoinExchange).should(times(0)).submitTransaction(any(Transaction.class));
    }
}
