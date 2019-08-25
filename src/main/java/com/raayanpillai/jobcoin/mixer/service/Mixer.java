package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;

public interface Mixer {
    DepositAddress createDepositAddress();

    void monitorDeposit(MixRequest mixRequest, DepositAddress depositAddress);

    void executeMix(MixRequest mixRequest);
}
