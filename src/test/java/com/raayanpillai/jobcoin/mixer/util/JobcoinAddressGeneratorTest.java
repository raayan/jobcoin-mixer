package com.raayanpillai.jobcoin.mixer.util;

import com.raayanpillai.jobcoin.mixer.model.Address;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class JobcoinAddressGeneratorTest {
    int addressLength = 20;
    JobcoinAddressGenerator jobcoinAddressGenerator = new JobcoinAddressGenerator(addressLength);

    @Test
    public void generateAddress() {
        Address generatedAddress = jobcoinAddressGenerator.generateAddress();

        System.out.println(generatedAddress);

        assertNotNull(generatedAddress);
        assertNotNull(generatedAddress.getAddress());
        assertEquals(addressLength, generatedAddress.getAddress().length());
    }
}