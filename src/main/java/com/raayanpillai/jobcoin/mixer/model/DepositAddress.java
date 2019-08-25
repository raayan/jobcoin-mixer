package com.raayanpillai.jobcoin.mixer.model;

import java.time.LocalDateTime;

public class DepositAddress extends Address implements Expires {
    private final LocalDateTime expiryDate;

    public DepositAddress(String address, LocalDateTime expiryDate) {
        super(address);
        this.expiryDate = expiryDate;
    }

    @Override
    public LocalDateTime getExpiryDate() {
        return expiryDate;
    }
}
