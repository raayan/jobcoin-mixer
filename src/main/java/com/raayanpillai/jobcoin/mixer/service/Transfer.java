package com.raayanpillai.jobcoin.mixer.service;


import com.raayanpillai.jobcoin.mixer.model.Address;

/**
 * A component that handles moving currency from one account to another
 * and checking balances
 */
public interface Transfer {
    Boolean transfer(Address fromAddress, Address toAddress, Float amount);

    Float getBalance(Address address);
}
