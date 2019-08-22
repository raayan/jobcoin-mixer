package com.raayanpillai.jobcoin.mixer.dto;

import java.util.List;

public class AddressInfoDTO {
    private Float balance;
    private List<TransactionDTO> transactions;

    public AddressInfoDTO() {
    }

    public Float getBalance() {
        return balance;
    }

    public void setBalance(Float balance) {
        this.balance = balance;
    }

    public List<TransactionDTO> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<TransactionDTO> transactions) {
        this.transactions = transactions;
    }
}
