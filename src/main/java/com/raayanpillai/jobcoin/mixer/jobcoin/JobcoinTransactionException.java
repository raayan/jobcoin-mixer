package com.raayanpillai.jobcoin.mixer.jobcoin;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;


public class JobcoinTransactionException extends JobcoinException {
    public JobcoinTransactionException(ErrorDTO errorDTO) {
        super("Transaction Failed", errorDTO);
    }
}
