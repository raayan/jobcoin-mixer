package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.*;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinAPI;
import com.raayanpillai.jobcoin.mixer.model.Address;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * A pass-through for interacting with the Jobcoin API
 */
@RestController
@RequestMapping("${api.path}/jobcoin")
public class JobcoinController {
    private static final Logger logger = LoggerFactory.getLogger(JobcoinController.class);

    private JobcoinAPI jobcoinApi;

    public JobcoinController(JobcoinAPI jobcoinApi) {
        this.jobcoinApi = jobcoinApi;
    }

    @GetMapping(path = "/addresses/{address}")
    @ResponseBody
    public AddressInfoDTO getAddressInfo(@PathVariable String address) {
        AddressInfoDTO addressInfoDTO = jobcoinApi.getAddressInfo(new Address(address)).block();

        return addressInfoDTO;
    }

    @GetMapping(path = "/transactions")
    @ResponseBody
    public TransactionDTO[] getTransactions() {
        TransactionDTO[] transactionDTOS = jobcoinApi.getTransactions().block();

        return transactionDTOS;
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
            @ApiResponse(code = 422, message = "Unprocessable Entity", response = ErrorDTO.class)
    })
    @PostMapping(path = "/transactions")
    @ResponseBody
    public ResponseDTO postTransaction(@RequestBody TransactionRequestDTO transactionRequestDTO) {
        ResponseDTO responseDTO = jobcoinApi.postTransaction(
                new Address(transactionRequestDTO.getFromAddress()),
                new Address(transactionRequestDTO.getToAddress()),
                transactionRequestDTO.getAmount())
                .block();

        return responseDTO;
    }
}
