package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.*;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.service.JobcoinApi;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * A pass-through for interacting with the Jobcoin API
 */
@RestController
@RequestMapping("/api/jobcoin")
public class JobcoinController {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinController.class);

    private JobcoinApi jobcoinApi;

    @Autowired
    public JobcoinController(JobcoinApi jobcoinApi) {
        this.jobcoinApi = jobcoinApi;
    }

    @GetMapping(path = "/addresses/{address}")
    @ResponseBody
    public AddressInfoDTO getAddressInfo(@PathVariable String address) {
        AddressInfoDTO addressInfo = jobcoinApi.getAddressInfo(new Address(address)).block();

        return addressInfo;
    }

    @GetMapping(path = "/transactions")
    @ResponseBody
    public TransactionDTO[] getTransactions() {
        TransactionDTO[] transactions = jobcoinApi.getTransactions().block();

        return transactions;
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorDTO.class)
    })
    @PostMapping(path = "/transactions")
    @ResponseBody
    public ResponseDTO postTransaction(@RequestBody TransactionRequestDTO transactionRequest) {
        ResponseDTO response = jobcoinApi.postTransaction(
                new Address(transactionRequest.getFromAddress()),
                new Address(transactionRequest.getToAddress()),
                transactionRequest.getAmount())
                .block();

        return response;
    }
}
