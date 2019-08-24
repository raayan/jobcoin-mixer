package com.raayanpillai.jobcoin.mixer.dto;

import java.util.Objects;

/**
 * Helper class to get the payload from the JobcoinApi
 */
public class ResponseDTO {
    private String status;

    public ResponseDTO() {
    }

    public ResponseDTO(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ResponseDTO that = (ResponseDTO) o;
        return Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(status);
    }

    @Override
    public String toString() {
        return "ResponseDTO{" +
                "status='" + status + '\'' +
                '}';
    }
}
