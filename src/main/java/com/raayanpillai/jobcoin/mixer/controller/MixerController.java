package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.dto.MixRequestDTO;
import com.raayanpillai.jobcoin.mixer.dto.MixResponseDTO;
import com.raayanpillai.jobcoin.mixer.exception.MixRequestException;
import com.raayanpillai.jobcoin.mixer.model.Address;
import com.raayanpillai.jobcoin.mixer.model.DepositAddress;
import com.raayanpillai.jobcoin.mixer.model.MixRequest;
import com.raayanpillai.jobcoin.mixer.service.Mixer;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Responsible for handling requests to mix jobcoins
 */
@RestController
@RequestMapping("${api.path}/mixer")
public class MixerController {
    private static final Logger logger = LoggerFactory.getLogger(MixerController.class);

    private final Mixer mixer;

    public MixerController(Mixer mixer) {
        this.mixer = mixer;
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request", response = ErrorDTO.class),
    })
    @PostMapping(path = "/mix")
    @ResponseBody
    public MixResponseDTO postMix(@RequestBody MixRequestDTO mixRequestDTO) throws MixRequestException {
        MixRequest mixRequest = processMixRequest(mixRequestDTO);

        DepositAddress depositAddress = mixer.createDepositAddress();

        mixer.monitorDeposit(mixRequest, depositAddress).subscribe(success ->
                        logger.info("Done {} {} {}", success, mixRequest, depositAddress),
                e -> logger.error("Funds not received, mix did not execute"));

        logger.info("Generated for user {}", depositAddress);
        return new MixResponseDTO(depositAddress.getAddress(), depositAddress.getExpiryDate());
    }

    protected MixRequest processMixRequest(MixRequestDTO mixRequestDTO) throws MixRequestException {
        MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
        if (mixRequestDTO != null) {
            if (mixRequestDTO.getAmount() == null || mixRequestDTO.getAmount() <= 0) {
                errorMap.add("amount", "Mix amount must be more than 0");
            }
            if (mixRequestDTO.getDestinations() == null || mixRequestDTO.getDestinations().isEmpty()) {
                errorMap.add("destinations", "You must specify at least 1 destination address");
            }
        } else {
            errorMap.add("request", "Request payload null!");
        }
        if (errorMap.size() > 0) {
            throw new MixRequestException(new ErrorDTO(errorMap));
        }

        assert mixRequestDTO != null;
        Float amountToMix = mixRequestDTO.getAmount();
        List<Address> destinationAddresses = mixRequestDTO.getDestinations()
                .stream().map(Address::new).collect(Collectors.toList());

        return new MixRequest(amountToMix, destinationAddresses);
    }
}
