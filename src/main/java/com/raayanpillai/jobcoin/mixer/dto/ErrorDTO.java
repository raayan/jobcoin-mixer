package com.raayanpillai.jobcoin.mixer.dto;

/**
 * Helper class to get the error payload from the JobcoinApi
 * Object is used for error as sometimes it is either a String or a Map
 */
public class ErrorDTO {
    private Object error;

    public ErrorDTO() {
    }

    public ErrorDTO(Object error) {
        this.error = error;
    }

    public Object getError() {
        return error;
    }

    public void setError(Object error) {
        this.error = error;
    }


    @Override
    public String toString() {
        return "ErrorDTO{" +
                "error=" + error +
                '}';
    }
}
