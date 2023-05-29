package com.simplifysynergy.swagger.firebase.auth.controller;

import com.simplifysynergy.swagger.firebase.auth.model.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.validation.ConstraintViolationException;

@ControllerAdvice
@Slf4j
public class ApiExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    ResponseEntity<ErrorResponse> handleIllegalArgument(IllegalArgumentException ex) {
        log.debug("handling handleIllegalArgument::" + ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .details(ex.getCause().toString()).build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(Exception.class)
    ResponseEntity<ErrorResponse> handelGeneralException(Exception ex) {
        log.debug("handling handelGeneralException::" + ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .details(ex.getCause().toString()).build(), HttpStatus.INTERNAL_SERVER_ERROR
        );
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ErrorResponse> handelConstraintViolationException(ConstraintViolationException ex) {
        log.debug("handling handelConstraintViolationException::" + ex);
        return new ResponseEntity<>(
                ErrorResponse.builder()
                        .message(ex.getMessage())
                        .details(ex.getConstraintViolations().toString()).build(), HttpStatus.BAD_REQUEST
        );
    }

}
