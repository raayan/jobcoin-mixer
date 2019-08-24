package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.MixRequestDTO;
import com.raayanpillai.jobcoin.mixer.dto.MixResponseDTO;
import com.raayanpillai.jobcoin.mixer.service.MixService;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

/**
 * Responsible for handling requests to mix jobcoins
 */
@RestController
@RequestMapping("${api.path}/mix")
public class MixController {
    private static final Logger logger = LoggerFactory.getLogger(MixController.class);

    private MixService mixService;

    public MixController(MixService mixService) {
        this.mixService = mixService;
    }

    @ApiResponses({
            @ApiResponse(code = 400, message = "Bad Request"),
    })
    @PostMapping(path = "/transactions")
    @ResponseBody
    public MixResponseDTO postMix(@RequestBody MixRequestDTO mixRequestDTO) {
        return new MixResponseDTO();
    }
}
