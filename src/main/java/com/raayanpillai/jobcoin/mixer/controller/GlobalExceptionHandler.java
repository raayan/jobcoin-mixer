package com.raayanpillai.jobcoin.mixer.controller;

import com.raayanpillai.jobcoin.mixer.dto.ErrorDTO;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinApiException;
import com.raayanpillai.jobcoin.mixer.exception.JobcoinTransactionException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Global Exception Handler for all RestControllers
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(JobcoinTransactionException.class)
    public ResponseEntity<ErrorDTO> handleException(JobcoinTransactionException e) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(new ErrorDTO(e.getMessage()));
    }

    @ExceptionHandler(JobcoinApiException.class)
    public ResponseEntity<ErrorDTO> handleException(JobcoinApiException e) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new ErrorDTO(e.getMessage()));
    }
}
