package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinRequestException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Mono;

import static java.util.Collections.EMPTY_LIST;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class JobcoinExchangeTest {

    @MockBean
    private JobcoinAPI jobcoinAPI;

    private JobcoinExchange jobcoinExchange;

    @Before
    public void setUp() throws Exception {
        jobcoinExchange = new JobcoinExchange(jobcoinAPI);
    }

    @Test
    public void getBalance_anyAddress_success() {
        Address address = new Address("testAddress");
        given(jobcoinAPI.getAddressInfo(address))
                .willReturn(Mono.just(new AddressInfoDTO(100f, EMPTY_LIST)));

        Float balance = jobcoinExchange.getBalance(address).block();

        assertEquals(100f, balance, 0);
    }

    @Test
    public void submitTransaction_response_true() {
        Transaction transaction = new Transaction(
                new Address("fromAddress"),
                new Address("toAddress"),
                100f);
        given(jobcoinAPI.postTransaction(transaction))
                .willReturn(Mono.just(new ResponseDTO("OK")));

        Boolean didTransact = jobcoinExchange.submitTransaction(transaction).block();

        assertTrue(didTransact);
    }

    @Test
    public void submitTransaction_JobcoinTransactionException_false() {
        Transaction transaction = new Transaction(
                new Address("fromAddress"),
                new Address("toAddress"),
                100f);
        given(jobcoinAPI.postTransaction(transaction))
                .willReturn(Mono.error(new JobcoinTransactionException(new ErrorDTO("Transaction issue"))));

        Boolean didTransact = jobcoinExchange.submitTransaction(transaction).block();

        assertFalse(didTransact);
    }


    @Test
    public void submitTransaction_JobcoinRequestException_false() {
        Transaction transaction = new Transaction(
                new Address("fromAddress"),
                new Address("toAddress"),
                100f);
        given(jobcoinAPI.postTransaction(transaction))
                .willReturn(Mono.error(new JobcoinRequestException(new ErrorDTO("Request issue"))));

        Boolean didTransact = jobcoinExchange.submitTransaction(transaction).block();

        assertFalse(didTransact);
    }
}