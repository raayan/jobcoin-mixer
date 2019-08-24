package com.raayanpillai.jobcoin.mixer.dto;

import java.util.Date;
import java.util.Objects;

public class MixResponseDTO {
    private String depositAddress;
    private Date expiryDate;

    public MixResponseDTO() {
    }

    public MixResponseDTO(String depositAddress, Date expiryDate) {
        this.depositAddress = depositAddress;
        this.expiryDate = expiryDate;
    }

    public String getDepositAddress() {
        return depositAddress;
    }

    public void setDepositAddress(String depositAddress) {
        this.depositAddress = depositAddress;
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixResponseDTO that = (MixResponseDTO) o;
        return Objects.equals(depositAddress, that.depositAddress) &&
                Objects.equals(expiryDate, that.expiryDate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(depositAddress, expiryDate);
    }

    @Override
    public String toString() {
        return "MixResponseDTO{" +
                "depositAddress='" + depositAddress + '\'' +
                ", expiryDate=" + expiryDate +
                '}';
    }
}
