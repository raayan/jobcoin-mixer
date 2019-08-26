package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import com.raayanpillai.jobcoin.mixer.exception.MixRequestException;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler globalExceptionHandler;

    @Before
    public void setUp() {
        globalExceptionHandler = new GlobalExceptionHandler();
    }

    @Test
    public void handleException_JobcoinTransactionException_ErrorDTO() {
        ResponseEntity<ErrorDTO> errorDTOResponseEntity = globalExceptionHandler.handleException(
                new JobcoinTransactionException(new ErrorDTO("Jobcoin Transaction Error")));

        assertNotNull(errorDTOResponseEntity);
        assertNotNull(errorDTOResponseEntity.getBody());
        assertEquals("Jobcoin Transaction Error", errorDTOResponseEntity.getBody().getError());
    }

    @Test
    public void handleException_JobcoinRequestException_ErrorDTO() {
        ResponseEntity<ErrorDTO> errorDTOResponseEntity = globalExceptionHandler.handleException(
                new JobcoinTransactionException(new ErrorDTO("Jobcoin Request Error")));

        assertNotNull(errorDTOResponseEntity);
        assertNotNull(errorDTOResponseEntity.getBody());
        assertEquals("Jobcoin Request Error", errorDTOResponseEntity.getBody().getError());
    }

    @Test
    public void handleException_MixRequestException_ErrorDTO() {
        ResponseEntity<ErrorDTO> errorDTOResponseEntity = globalExceptionHandler.handleException(
                new MixRequestException(new ErrorDTO("Mix Request Error")));

        assertNotNull(errorDTOResponseEntity);
        assertNotNull(errorDTOResponseEntity.getBody());
        assertEquals("Mix Request Error", errorDTOResponseEntity.getBody().getError());


    }
}