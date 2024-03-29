package com.raayanpillai.jobcoin.mixer.exception;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;

/**
 * Thrown when the user makes a bad mix request
 */
public class MixRequestException extends MixException {
    private ErrorDTO errorDTO;

    public MixRequestException(ErrorDTO errorDTO) {
        super(null);
        this.errorDTO = errorDTO;
    }

    public ErrorDTO getErrorDTO() {
        return errorDTO;
    }
}
