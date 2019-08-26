package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.MixRequestDTO;
import com.raayanpillai.jobcoin.mixer.dto.MixResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.MixRequestException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.service.Mixer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@RunWith(SpringRunner.class)
public class MixerControllerTest {

    @MockBean
    private Mixer mixer;

    private MixerController mixerController;

    @Before
    public void setUp() {
        mixerController = new MixerController(mixer);
    }

    @Test
    public void postMix() throws MixRequestException {
        DepositAddress depositAddress = new DepositAddress("testDeposit", LocalDateTime.now());

        given(mixer.createDepositAddress()).willReturn(depositAddress);
        given(mixer.monitorDeposit(any(MixRequest.class), any(DepositAddress.class))).willReturn(Flux.just(true));

        MixRequestDTO mixRequestDTO = new MixRequestDTO();
        mixRequestDTO.setAmount(100f);
        mixRequestDTO.setDestinations(singletonList("testDestination"));

        MixResponseDTO result = mixerController.postMix(mixRequestDTO);

        assertNotNull(result);
        assertEquals(depositAddress.getAddress(), result.getDepositAddress());
        assertEquals(depositAddress.getExpiryDate(), result.getExpiryDate());
    }

    @Test
    public void processMixRequest_allFieldsPresent_success() throws MixRequestException {
        MixRequestDTO mixRequestDTO = new MixRequestDTO();
        mixRequestDTO.setAmount(100f);
        mixRequestDTO.setDestinations(singletonList("testDestination"));

        MixRequest mixRequest = mixerController.processMixRequest(mixRequestDTO);

        assertNotNull(mixRequest);
        assertEquals(100f, mixRequest.getAmount(), 0.0);
        assertEquals(singletonList(new Address("testDestination")),
                mixRequest.getDestinations());
    }

    @Test(expected = MixRequestException.class)
    public void processMixRequest_nullRequest_errors() throws MixRequestException {
        mixerController.processMixRequest(null);
    }

    @Test(expected = MixRequestException.class)
    public void processMixRequest_missingFields_errors() throws MixRequestException {
        MixRequestDTO mixRequestDTO = new MixRequestDTO();
        mixRequestDTO.setAmount(null);
        mixRequestDTO.setDestinations(null);

        mixerController.processMixRequest(mixRequestDTO);
    }

    @Test(expected = MixRequestException.class)
    public void processMixRequest_badFields_errors() throws MixRequestException {
        MixRequestDTO mixRequestDTO = new MixRequestDTO();
        mixRequestDTO.setAmount(-10f);
        mixRequestDTO.setDestinations(EMPTY_LIST);

        mixerController.processMixRequest(mixRequestDTO);
    }
}