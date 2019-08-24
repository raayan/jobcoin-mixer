package com.raayanpillai.jobcoin.mixer.model;

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
    public String toString() {
        return "Transaction{" +
                "fromAddress=" + fromAddress +
                ", toAddress=" + toAddress +
                ", amount=" + amount +
                '}';
    }
}
