package com.raayanpillai.jobcoin.mixer.dto;

import javax.validation.constraints.NotNull;

public class TransactionRequestDTO {
    @NotNull
    private String fromAddress;
    @NotNull
    private String toAddress;
    @NotNull
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
