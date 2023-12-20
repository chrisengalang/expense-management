package dev.chrisen.em.controller;

import dev.chrisen.em.exception.RegistrationDuplicateException;
import dev.chrisen.em.model.GenericResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RegistrationDuplicateException.class)
    public final ResponseEntity<GenericResponse> handleRegistrationDuplicateException(RegistrationDuplicateException ex) {
        return new ResponseEntity<>(new GenericResponse(ex.getMessage(), HttpStatus.BAD_REQUEST), HttpStatus.BAD_REQUEST);
    }

}
