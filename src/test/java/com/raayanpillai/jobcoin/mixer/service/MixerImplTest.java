package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.util.AddressGenerator;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;

import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

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
    public void monitorDeposit() {

    }

    @Test
    public void executeMix() {
    }
}