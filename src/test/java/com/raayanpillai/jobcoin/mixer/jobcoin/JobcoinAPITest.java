package com.raayanpillai.jobcoin.mixer.jobcoin;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.ResponseDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.model.Address;
import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.After;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

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
    public void getTransactions() {
        List<TransactionDTO> expectedTransactions = Arrays.asList(
                new TransactionDTO(null, "fromTest", "toTest", 50f),
                new TransactionDTO(null, "toTest", "fromTest", 50f));


        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("[{\"timestamp\":\"null\",\"fromAddress\": \"fromTest\",\"toAddress\":\"toTest\",\"amount\":50.0},{\"timestamp\":\"null\",\"fromAddress\": \"toTest\",\"toAddress\":\"fromTest\",\"amount\":50.0}]")
        );

        List<TransactionDTO> transactions = Arrays.asList(jobcoinApi.getTransactions().block());

        assertEquals(expectedTransactions, transactions);
    }

    @Test
    public void postTransaction() {
        ResponseDTO expectedResponse = new ResponseDTO("OK");

        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(200)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"status\": \"OK\"}")
        );

        ResponseDTO response = jobcoinApi.postTransaction(
                new Address("fromAddress"),
                new Address("toAddress"),
                100f)
                .block();

        assertEquals(expectedResponse, response);
    }

    @Test(expected = Exception.class)
    public void postTransaction_exception() {
        mockWebServer.enqueue(
                new MockResponse()
                        .setResponseCode(422)
                        .setHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .setBody("{\"error\": \"insufficient funds\"}")
        );

        jobcoinApi.postTransaction(
                new Address("fromAddress"),
                new Address("toAddress"),
                100f)
                .block();
    }
}