package com.raayanpillai.jobcoin.mixer.model;

import java.util.List;
import java.util.Objects;

public class MixRequest {
    private Float amount;
    private List<Address> destinations;

    public MixRequest(Float amount, List<Address> destinations) {
        this.amount = amount;
        this.destinations = destinations;
    }

    public Float getAmount() {
        return amount;
    }

    public List<Address> getDestinations() {
        return destinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixRequest that = (MixRequest) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(destinations, that.destinations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, destinations);
    }

    @Override
    public String toString() {
        return "MixRequest{" +
                "amount=" + amount +
                ", destinations=" + destinations +
                '}';
    }
}
