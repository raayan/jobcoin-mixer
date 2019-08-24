package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinRequestException;
import com.raayanpillai.jobcoin.mixer.jobcoin.JobcoinTransactionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler for all RestControllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(JobcoinTransactionException.class)
    public ResponseEntity<ErrorDTO> handleException(JobcoinTransactionException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(e.getErrorDTO());
    }

    @ExceptionHandler(JobcoinRequestException.class)
    public ResponseEntity<ErrorDTO> handleException(JobcoinRequestException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(e.getErrorDTO());
    }
}
