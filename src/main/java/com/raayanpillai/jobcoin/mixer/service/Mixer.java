package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The primary service in this application, it handles deciding how to mix
 * and executing those mixes
 */
@Service
public class Mixer {
    private static final Logger logger = LoggerFactory.getLogger(Mixer.class);

    private Transfer transfer;

    public Mixer(Transfer transfer) {
        this.transfer = transfer;
    }

    public void startMix(MixRequest mixRequest) {
        logger.info("MixRequest: {}", mixRequest);

    }


}
