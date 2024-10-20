package com.brok.handler;

import com.brok.exeption.AmountException;
import com.brok.exeption.WalletNotFoundException;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Log4j2
public class GlobalExceptionHandler {
    @ExceptionHandler(value = WalletNotFoundException.class)
    ResponseEntity handleUserException(WalletNotFoundException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = AmountException.class)
    ResponseEntity handleUserException(AmountException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }

    @ExceptionHandler(value = Exception.class)
    ResponseEntity handleUserException(Exception e) {
        log.error(e);
        return ResponseEntity.internalServerError().body("An error occurred");
    }
}
