package com.raayanpillai.jobcoin.mixer.dto;

public class TransactionRequestDTO {
    private String fromAddress;
    private String toAddress;
    private Float amount;

    public TransactionRequestDTO() {
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
}
