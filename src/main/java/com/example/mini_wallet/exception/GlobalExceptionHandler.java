
package com.example.mini_wallet.exception;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<Map<String, String>> handleDuplicateEmail(
            EmailAlreadyExistsException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "errorCode", "EMAIL_ALREADY_EXISTS",
                        "message", "A wallet already exists for this email"
                ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidation(
            MethodArgumentNotValidException ex) {

        String message = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .findFirst()
                .map(error -> error.getDefaultMessage())
                .orElse("Request validation failed");

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "errorCode", "VALIDATION_ERROR",
                        "message", message
                ));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<Map<String, String>> handleDataIntegrity(
            DataIntegrityViolationException ex) {

        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(Map.of(
                        "errorCode", "DATA_INTEGRITY_VIOLATION",
                        "message", "The request conflicts with a database constraint"
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, String>> handleUnreadableRequest(
            HttpMessageNotReadableException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "errorCode", "INVALID_REQUEST_BODY",
                        "message", "The request body is missing or contains invalid data"
                ));
    }

    @ExceptionHandler(WalletNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleWalletNotFound(
            WalletNotFoundException ex) {

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of(
                        "errorCode", "WALLET_NOT_FOUND",
                        "message", ex.getMessage()
                ));
    }


    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<Map<String, String>> handleInsufficientBalance(
            InsufficientBalanceException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "errorCode", "INSUFFICIENT_BALANCE",
                        "message", "Insufficient balance to complete the transfer"
                ));
    }


    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgument(
            IllegalArgumentException ex) {

        return ResponseEntity.badRequest()
                .body(Map.of(
                        "errorCode", "INVALID_OPERATION",
                        "message", ex.getMessage()
                ));
    }



}
