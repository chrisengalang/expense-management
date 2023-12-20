package dev.chrisen.em.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.chrisen.em.exception.RegistrationDuplicateException;
import dev.chrisen.em.model.GenericResponse;
import dev.chrisen.em.model.RegistrationRequest;
import dev.chrisen.em.service.RegistrationServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static dev.chrisen.em.constant.ResponseMessageConstants.REGISTRATION_DUPLICATE_USERNAME;
import static dev.chrisen.em.constant.ResponseMessageConstants.REGISTRATION_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
public class RegistrationControllerTest {

    private MockMvc mockMvc;

    @MockBean
    private RegistrationServiceImpl registrationService;

    private ObjectMapper mapper;

    @BeforeEach
    public void setup() {
        registrationService = Mockito.mock(RegistrationServiceImpl.class);
        RegistrationController registrationController = new RegistrationController(registrationService);

        mockMvc = MockMvcBuilders
                .standaloneSetup(registrationController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        mapper = new ObjectMapper();
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        RegistrationRequest request = new RegistrationRequest();

        request.setUsername("chrisengalang");
        request.setName("Chrisen");

        MvcResult result = mockMvc.perform(
                post("/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andReturn();

        GenericResponse response = mapper.readValue(
                result.getResponse().getContentAsString(), GenericResponse.class);

        assertEquals(response.getMessage(), REGISTRATION_SUCCESS);
        assertEquals(response.getStatus(), HttpStatus.CREATED);
        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
    }

    @Test
    public void testRegisterUserDuplicateUsername_Failed() throws Exception {
        doThrow(new RegistrationDuplicateException(REGISTRATION_DUPLICATE_USERNAME))
                .when(registrationService).register(any(RegistrationRequest.class));

        RegistrationRequest request = new RegistrationRequest();

        request.setUsername("chrisengalang");
        request.setName("Chrisen");

        MvcResult result = mockMvc.perform(
                        post("/register")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andReturn();

        GenericResponse response = mapper.readValue(
                result.getResponse().getContentAsString(), GenericResponse.class);

        verify(registrationService, times(1)).register(any(RegistrationRequest.class));
        assertEquals(response.getMessage(), REGISTRATION_DUPLICATE_USERNAME);
        assertEquals(response.getStatus(), HttpStatus.BAD_REQUEST);
    }

}
