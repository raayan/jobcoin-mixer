package com.raayanpillai.jobcoin.mixer.exception;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;


public class JobcoinTransactionException extends JobcoinException {
    public JobcoinTransactionException(ErrorDTO errorDTO) {
        super("Transaction Failed", errorDTO);
    }
}
