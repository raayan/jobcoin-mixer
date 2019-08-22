package com.raayanpillai.jobcoin.mixer.jcapi;

public class ApiResponse {
    private String status;
    private String error;

    public ApiResponse() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
