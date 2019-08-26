package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.MixRequestDTO;
import com.raayanpillai.jobcoin.mixer.exception.MixRequestException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.service.Mixer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Collections.EMPTY_LIST;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(SpringRunner.class)
public class MixerControllerTest {

    @MockBean
    private Mixer mixer;

    private MixerController mixerController;

    @Before
    public void setUp() throws Exception {
        mixerController = new MixerController(mixer);
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
                mixRequest.getDestinationAddresses());
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