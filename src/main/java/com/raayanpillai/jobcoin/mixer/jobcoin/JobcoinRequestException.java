package com.raayanpillai.jobcoin.mixer.jobcoin;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;

public class JobcoinRequestException extends JobcoinException {
    public JobcoinRequestException(ErrorDTO errorDTO) {
        super("Bad Request", errorDTO);
    }
}
