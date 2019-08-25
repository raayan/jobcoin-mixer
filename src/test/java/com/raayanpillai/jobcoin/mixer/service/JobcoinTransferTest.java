package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinAPI;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

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

    @MockBean
    private JobcoinAPI jobcoinAPI;

    private JobcoinTransfer jobcoinTransfer;

    @Before
    public void setUp() throws Exception {
        jobcoinTransfer = new JobcoinTransfer(jobcoinAPI);

        fromAddress = new Address("fromAddress");
        toAddress = new Address("toAddress");
        amount = 100F;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void move() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.just(new ResponseDTO("OK")));

        Boolean result = jobcoinTransfer.move(toAddress, fromAddress, amount);

        assertTrue(result);
    }

    @Test
    public void move_transactionException() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.error(new JobcoinTransactionException(new ErrorDTO("INSUFFICIENT FUNDS"))));

        Boolean result = jobcoinTransfer.move(toAddress, fromAddress, amount);

        assertFalse(result);
    }

    @Test
    public void getBalance() {
        given(jobcoinAPI.getAddressInfo(any(Address.class)))
                .willReturn(Mono.just(new AddressInfoDTO(amount, new ArrayList<>())));


        Float result = jobcoinTransfer.getBalance(fromAddress);

        assertEquals(amount, result);
    }
}