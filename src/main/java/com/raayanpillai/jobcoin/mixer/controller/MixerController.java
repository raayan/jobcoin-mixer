package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.AddressInfoDTO;
import com.raayanpillai.jobcoin.mixer.dto.TransactionDTO;
import com.raayanpillai.jobcoin.mixer.jcapi.ApiResponse;
import com.raayanpillai.jobcoin.mixer.jcapi.JobCoinApi;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mixer")
public class MixerController {

    private JobCoinApi jobCoinApi;

    @Autowired
    public MixerController(JobCoinApi jobCoinApi) {
        this.jobCoinApi = jobCoinApi;
    }

    @GetMapping(path = "/address/{address}")
    public AddressInfoDTO getAddressInfo(@PathVariable String address) {
        AddressInfoDTO addressInfo = jobCoinApi.getAddressInfo(address);

        return addressInfo;
    }

    @GetMapping(path = "/transactions")
    public List<TransactionDTO> getTransactions() {
        List<TransactionDTO> transactions = jobCoinApi.getTransactions();

        return transactions;
    }

    @PostMapping(path = "/transactions")
    public ApiResponse postTransaction(@RequestBody TransactionDTO transaction) {
        ResponseEntity<ApiResponse> responseEntity = jobCoinApi.postTransaction(transaction);

        return responseEntity.getBody();
    }
}
