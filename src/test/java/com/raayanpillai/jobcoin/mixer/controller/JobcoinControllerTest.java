package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionRequestDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinAPI;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class JobcoinControllerTest {

    @MockBean
    private JobcoinAPI jobcoinAPI;

    private JobcoinController jobcoinController;

    @Before
    public void setUp() {
        jobcoinController = new JobcoinController(jobcoinAPI);
    }

    @Test
    public void getAddressInfo() {
        AddressInfoDTO addressInfoDTO = new AddressInfoDTO(100f, EMPTY_LIST);

        given(jobcoinAPI.getAddressInfo(new Address("Test")))
                .willReturn(Mono.just(addressInfoDTO));

        AddressInfoDTO result = jobcoinController.getAddressInfo("Test");

        assertEquals(addressInfoDTO, result);
    }

    @Test
    public void getTransactions() {
        List<TransactionDTO> transactionDTOList = singletonList(new TransactionDTO());

        given(jobcoinAPI.getTransactions()).willReturn(Flux.fromIterable(transactionDTOList));

        List<TransactionDTO> result = jobcoinController.getTransactions();

        assertEquals(transactionDTOList, result);
    }

    @Test
    public void postTransaction() {
        ResponseDTO responseDTO = new ResponseDTO("OK");

        given(jobcoinAPI.postTransaction(any(Transaction.class))).willReturn(Mono.just(responseDTO));

        ResponseDTO result = jobcoinController.postTransaction(new TransactionRequestDTO());

        assertEquals(responseDTO, result);
    }
}