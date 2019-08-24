package com.raayanpillai.jobcoin.mixer.dto;

/**
 * Helper class to get the error payload from the JobcoinApi
 */
public class ErrorDTO {
    private String error;

    public ErrorDTO() {
    }

    public ErrorDTO(String error) {
        this.error = error;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
