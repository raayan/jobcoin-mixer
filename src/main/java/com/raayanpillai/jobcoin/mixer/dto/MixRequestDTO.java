package com.raayanpillai.jobcoin.mixer.dto;

import java.util.List;
import java.util.Objects;

public class MixRequestDTO {
    private Float amount;
    private List<String> destinations;

    public MixRequestDTO() {
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MixRequestDTO that = (MixRequestDTO) o;
        return Objects.equals(amount, that.amount) &&
                Objects.equals(destinations, that.destinations);
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount, destinations);
    }

    @Override
    public String toString() {
        return "MixRequestDTO{" +
                "amount=" + amount +
                ", destinations=" + destinations +
                '}';
    }
}
