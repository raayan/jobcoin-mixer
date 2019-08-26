package com.raayanpillai.jobcoin.mixer.exception;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;

/**
 * Thrown when the transfer fails to transact funds
 */
public class MixTransferException extends MixException {
    private ErrorDTO errorDTO;

    public MixTransferException(ErrorDTO errorDTO) {
        super(null);
        this.errorDTO = errorDTO;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
