package com.raayanpillai.jobcoin.mixer.dto;

import java.util.List;
import java.util.Objects;

public class AddressInfoDTO {
    private Float balance;
    private List<TransactionDTO> transactions;

    public AddressInfoDTO() {
    }

    public AddressInfoDTO(Float balance, List<TransactionDTO> transactions) {
        this.balance = balance;
        this.transactions = transactions;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressInfoDTO that = (AddressInfoDTO) o;
        return Objects.equals(balance, that.balance) &&
                Objects.equals(transactions, that.transactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(balance, transactions);
    }

    @Override
    public String toString() {
        return "AddressInfoDTO{" +
                "balance=" + balance +
                ", transactions=" + transactions +
                '}';
    }
}
