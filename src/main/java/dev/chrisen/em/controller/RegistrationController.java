package dev.chrisen.em.controller;

import dev.chrisen.em.model.GenericResponse;
import dev.chrisen.em.model.RegistrationRequest;
import dev.chrisen.em.service.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static dev.chrisen.em.constant.ResponseMessageConstants.REGISTRATION_SUCCESS;

@RestController
@RequestMapping("/register")
@RequiredArgsConstructor
public class RegistrationController {

    private final RegistrationService registrationService;

    @PostMapping
    public ResponseEntity<GenericResponse> register(@RequestBody RegistrationRequest request) {
        registrationService.register(request);
        return new ResponseEntity<>(
                new GenericResponse(REGISTRATION_SUCCESS, HttpStatus.CREATED),
                HttpStatus.CREATED);
    }

}
