package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.*;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.Transaction;
import com.raayanpillai.jobcoin.mixer.repository.JobcoinAPI;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * A pass-through for interacting with the Jobcoin API
 */
@RestController
@RequestMapping("${api.path}/jobcoin")
public class JobcoinController {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinController.class);

    private final JobcoinAPI jobcoinApi;

    public JobcoinController(JobcoinAPI jobcoinApi) {
        this.jobcoinApi = jobcoinApi;
    }

    @GetMapping(path = "/addresses/{address}")
    @ResponseBody
    public AddressInfoDTO getAddressInfo(@PathVariable String address) {
        return jobcoinApi.getAddressInfo(new Address(address)).block();
    }

    @GetMapping(path = "/transactions")
    @ResponseBody
    public List<TransactionDTO> getTransactions() {
        return jobcoinApi.getTransactions().collectList().block();
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorDTO.class)
    })
    @PostMapping(path = "/transactions")
    @ResponseBody
    public ResponseDTO postTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        Transaction transaction = new Transaction(
                new Address(transactionRequestDTO.getFromAddress()),
                new Address(transactionRequestDTO.getToAddress()),
                transactionRequestDTO.getAmount());

        return jobcoinApi.postTransaction(transaction).block();
    }
}
