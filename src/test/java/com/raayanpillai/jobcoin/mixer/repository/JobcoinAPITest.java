package com.raayanpillai.jobcoin.mixer.repository;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class JobcoinAPITest {

    private final MockWebServer mockWebServer = new MockWebServer();
    private final JobcoinAPI jobcoinApi = new JobcoinAPI(WebClient.create(mockWebServer.url("/").toString()));

    @After
    public void tearDown() throws Exception {
        mockWebServer.shutdown();
    }

    @Test
    public void getAddressInfo() {
        AddressInfoDTO expectedAddressInfo = new AddressInfoDTO(
                50f,
                Arrays.asList(
                        new TransactionDTO(
                                null,
                                "fromTest",
                                "toTest",
                                50f)));

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"balance\":50.0,\"transactions\":[{\"timestamp\":\"null\",\"fromAddress\": \"fromTest\",\"toAddress\":\"toTest\",\"amount\":50.0}]}")
        );


        AddressInfoDTO addressInfo = jobcoinApi.getAddressInfo(new Address("toTest")).block();

        assertEquals(expectedAddressInfo, addressInfo);
    }

    @Test
    public void getTransactions_all_list() {
        List<TransactionDTO> expectedTransactions = Arrays.asList(
                new TransactionDTO(null, "fromTest", "toTest", 50f),
                new TransactionDTO(null, "toTest", "fromTest", 50f));


        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("[{\"timestamp\":\"null\",\"fromAddress\": \"fromTest\",\"toAddress\":\"toTest\",\"amount\":50.0},{\"timestamp\":\"null\",\"fromAddress\": \"toTest\",\"toAddress\":\"fromTest\",\"amount\":50.0}]")
        );

        List<TransactionDTO> transactions = jobcoinApi.getTransactions().collectList().block();

        assertEquals(expectedTransactions, transactions);
    }

    @Test
    public void getTransactions_just2_list() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("[{\"timestamp\":\"null\",\"fromAddress\": \"fromTest\",\"toAddress\":\"toTest\",\"amount\":50.0},{\"timestamp\":\"null\",\"fromAddress\": \"toTest\",\"toAddress\":\"fromTest\",\"amount\":50.0}]")
        );

        List<TransactionDTO> transactions = jobcoinApi.getTransactions().take(1).collectList().block();

        assertNotNull(transactions);
        assertEquals(1, transactions.size());
    }

    @Test
    public void postTransaction() {
        ResponseDTO expectedResponse = new ResponseDTO("OK");
        Transaction transaction = new Transaction(new Address("fromAddress"), new Address("toAddress"), 10f);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"status\": \"OK\"}")
        );

        ResponseDTO response = jobcoinApi.postTransaction(transaction).block();

        assertEquals(expectedResponse, response);
    }

    @Test(expected = Exception.class)
    public void postTransaction_exception() {
        Transaction transaction = new Transaction(new Address("fromAddress"), new Address("toAddress"), 10f);

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(422)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"insufficient funds\"}")
        );

        jobcoinApi.postTransaction(transaction).block();
    }
}