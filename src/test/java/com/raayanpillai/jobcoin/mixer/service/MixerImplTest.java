package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.MixTransferException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

@RunWith(SpringRunner.class)
public class MixerImplTest {
    private Address houseAddress;
    private long expiresInSeconds;

    @MockBean
    private Transfer transfer;

    @MockBean
    private Executor executor;

    @MockBean
    private AddressGenerator addressGenerator;

    private MixerImpl mixer;

    @Before
    public void setUp() throws Exception {
        houseAddress = new Address("testHouse");
        expiresInSeconds = 60;

        mixer = new MixerImpl(transfer, executor, addressGenerator, houseAddress, expiresInSeconds);
    }

    @Test
    public void createDepositAddress() {
        Address expectedAddress = new Address("testRandom");

        given(addressGenerator.generateAddress()).willReturn(expectedAddress);

        DepositAddress depositAddress = mixer.createDepositAddress();

        assertNotNull(depositAddress);
        assertEquals(expectedAddress.getAddress(), depositAddress.getAddress());
        assertTrue(depositAddress.getExpiryDate().isAfter(LocalDateTime.now()));
    }

    @Test
    public void monitorDeposit_transfer_scheduled() {
        MixRequest mixRequest = new MixRequest(100f, Collections.singletonList(new Address("testDestination")));
        DepositAddress depositAddress = new DepositAddress("testDeposit",
                LocalDateTime.now().plusSeconds(expiresInSeconds));

        given(transfer.watchAndTransact(any(Transaction.class), any(Duration.class), any(Duration.class)))
                .willReturn(Flux.just(true));

        mixer.monitorDeposit(mixRequest, depositAddress);

        then(executor).should(times(1)).scheduleTransaction(any(Transaction.class));
    }

    @Test
    public void monitorDeposit_transferFailure_notScheduled() {
        MixRequest mixRequest = new MixRequest(100f, Collections.singletonList(new Address("testDestination")));
        DepositAddress depositAddress = new DepositAddress("testDeposit",
                LocalDateTime.now().plusSeconds(expiresInSeconds));

        given(transfer.watchAndTransact(any(Transaction.class), any(Duration.class), any(Duration.class)))
                .willReturn(Flux.error(new MixTransferException(new ErrorDTO("Balance insufficient"))));

        mixer.monitorDeposit(mixRequest, depositAddress);

        then(executor).shouldHaveZeroInteractions();
    }


    @Test
    public void executeMix() {
        MixRequest mixRequest = new MixRequest(100f, Arrays.asList(
                new Address("testDestination1"),
                new Address("testDestination2"),
                new Address("testDestination3")));

        mixer.executeMix(mixRequest);

        then(executor).should(times(3)).scheduleTransaction(any(Transaction.class));
    }
}