package com.raayanpillai.jobcoin.mixer.model;

import java.util.Objects;

public class Transaction {
    private Address fromAddress;
    private Address toAddress;
    private Float amount;

    public Transaction(Address fromAddress, Address toAddress, Float amount) {
        this.fromAddress = fromAddress;
        this.toAddress = toAddress;
        this.amount = amount;
    }

    public Address getFromAddress() {
        return fromAddress;
    }

    public void setFromAddress(Address fromAddress) {
        this.fromAddress = fromAddress;
    }

    public Address getToAddress() {
        return toAddress;
    }

    public void setToAddress(Address toAddress) {
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
        Transaction that = (Transaction) o;
        return Objects.equals(fromAddress, that.fromAddress) &&
                Objects.equals(toAddress, that.toAddress) &&
                Objects.equals(amount, that.amount);
    }

    @Override
    public int hashCode() {
        return Objects.hash(fromAddress, toAddress, amount);
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "fromAddress=" + fromAddress +
                ", toAddress=" + toAddress +
                ", amount=" + amount +
                '}';
    }
}
