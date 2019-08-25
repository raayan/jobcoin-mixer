package com.raayanpillai.jobcoin.mixer.util;

import com.raayanpillai.jobcoin.mixer.model.Address;

public class JobcoinAddressGenerator implements AddressGenerator {
    private static final String ALPHA_NUMERIC_STRING = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int addressLength;

    public JobcoinAddressGenerator(int addressLength) {
        this.addressLength = addressLength;
    }

    @Override
    public Address generateAddress() {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < addressLength; i++) {
            int character = (int) (Math.random() * ALPHA_NUMERIC_STRING.length());
            builder.append(ALPHA_NUMERIC_STRING.charAt(character));
        }

        String addressString = builder.toString();

        return new Address(addressString);
    }
}
