package com.raayanpillai.jobcoin.mixer.exception;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;

public abstract class JobcoinException extends Exception {
    private ErrorDTO errorDTO;

    public JobcoinException(String message, ErrorDTO errorDTO) {
        super(message);
        this.errorDTO = errorDTO;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
