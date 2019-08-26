package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import reactor.core.publisher.Flux;

public interface Mixer {
    DepositAddress createDepositAddress();

    Flux<Boolean> monitorDeposit(MixRequest mixRequest, DepositAddress depositAddress);

    Boolean executeMix(MixRequest mixRequest);
}
