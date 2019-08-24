package com.raayanpillai.jobcoin.mixer.transfer;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinAPI;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
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

    Address fromAddress;
    Address toAddress;
    Float amount;

    @MockBean
    private JobcoinAPI jobcoinAPI;

    private JobcoinTransfer jobcoinTransfer;

    @Before
    public void setUp() throws Exception {
        jobcoinTransfer = new JobcoinTransfer(jobcoinAPI);

        fromAddress = new Address("fromAddress");
        toAddress = new Address("toAddress");
        amount = 100f;
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void transfer() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.just(new ResponseDTO("OK")));

        Boolean result = jobcoinTransfer.transfer(toAddress, fromAddress, amount);

        assertTrue(result);
    }

    @Test
    public void transfer_transactionException() {
        given(jobcoinAPI.postTransaction(any(Address.class), any(Address.class), anyFloat()))
                .willReturn(Mono.error(new JobcoinTransactionException(new ErrorDTO("INSUFFICIENT FUNDS"))));

        Boolean result = jobcoinTransfer.transfer(toAddress, fromAddress, amount);

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