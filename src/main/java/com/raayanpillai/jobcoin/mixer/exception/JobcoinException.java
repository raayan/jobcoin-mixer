package com.raayanpillai.jobcoin.mixer.exception;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;

public abstract class JobcoinException extends MixException {
    private ErrorDTO errorDTO;

    JobcoinException(String message, ErrorDTO errorDTO) {
        super(message);
        this.errorDTO = errorDTO;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
