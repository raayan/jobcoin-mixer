package com.raayanpillai.jobcoin.mixer.dto;

import java.util.Date;
import java.util.Objects;

public class TransactionDTO {
    private Date timestamp;
    private String fromAddress;
    private String toAddress;
    private Float amount;

    public TransactionDTO() {
    }

    public TransactionDTO(Date timestamp, String fromAddress, String toAddress, Float amount) {
        this.timestamp = timestamp;
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public String getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(String fromAddress) {
        this.fromAddress = fromAddress;
    }

    public String getToAddress() {
        return toAddress;
    }

    public void setToAddress(String toAddress) {
        this.toAddress = toAddress;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TransactionDTO that = (TransactionDTO) o;
        return Objects.equals(timestamp, that.timestamp) &&
                Objects.equals(fromAddress, that.fromAddress) &&
                Objects.equals(toAddress, that.toAddress) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(timestamp, fromAddress, toAddress, amount);
    }

    @Override
    public String toString() {
        return "TransactionDTO{" +
                "timestamp=" + timestamp +
                ", fromAddress='" + fromAddress + '\'' +
                ", toAddress='" + toAddress + '\'' +
                ", amount=" + amount +
                '}';
    }
}
