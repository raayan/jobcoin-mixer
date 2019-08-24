package com.raayanpillai.jobcoin.mixer.service;

import com.raayanpillai.jobcoin.mixer.transfer.Transfer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * The primary service in this application, it handles deciding how to mix
 * and executing those mixes
 */
@Service
public class MixService {
    private static final Logger logger = LoggerFactory.getLogger(MixService.class);

    private Transfer transfer;

    public MixService(Transfer transfer) {
        this.transfer = transfer;
    }


}
