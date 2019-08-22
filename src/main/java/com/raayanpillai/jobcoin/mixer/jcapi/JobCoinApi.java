package com.raayanpillai.jobcoin.mixer.jcapi;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Component
public class JobCoinApi {
    private static final Logger logger = LoggerFactory.getLogger(JobCoinApi.class);

    @Value("${mixer.api.uri}")
    private String baseApiUri;

    private RestTemplate restTemplate;

    @Autowired
    public JobCoinApi(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public AddressInfoDTO getAddressInfo(String address) {
        ResponseEntity<AddressInfoDTO> responseEntity = restTemplate.exchange(
                baseApiUri + "/addresses/" + address,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<AddressInfoDTO>() {
                });

        return responseEntity.getBody();
    }

    public List<TransactionDTO> getTransactions() {
        ResponseEntity<List<TransactionDTO>> responseEntity = restTemplate.exchange(
                baseApiUri + "/transactions",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<TransactionDTO>>() {
                });

        return responseEntity.getBody();
    }

    public ResponseEntity<ApiResponse> postTransaction(TransactionDTO transactionDTO) {
        ResponseEntity<ApiResponse> responseEntity = restTemplate.postForEntity(
                baseApiUri + "/transactions",
                transactionDTO,
                ApiResponse.class);

        return responseEntity;
    }
}
