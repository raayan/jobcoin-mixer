package com.raayanpillai.jobcoin.mixer.model;

import java.util.List;
import java.util.Objects;

public class MixRequest {
    private Float amount;
    private List<Address> destinationAddresses;

    public MixRequest(Float amount, List<Address> destinationAddresses) {
        this.amount = amount;
        this.destinationAddresses = destinationAddresses;
    }

    public Float getAmount() {
        return amount;
    }

    public List<Address> getDestinationAddresses() {
        return destinationAddresses;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixRequest that = (MixRequest) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(destinationAddresses, that.destinationAddresses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, destinationAddresses);
    }

    @Override
    public String toString() {
        return "MixRequest{" +
                "amount=" + amount +
                ", destinationAddresses=" + destinationAddresses +
                '}';
    }
}
