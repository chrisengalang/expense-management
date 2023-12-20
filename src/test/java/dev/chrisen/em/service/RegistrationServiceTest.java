package dev.chrisen.em.service;

import dev.chrisen.em.exception.RegistrationDuplicateException;
import dev.chrisen.em.model.RegistrationRequest;
import dev.chrisen.em.model.User;
import dev.chrisen.em.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class RegistrationServiceTest {

    UserRepository repository;

    PasswordEncoder passwordEncoder;

    RegistrationServiceImpl registrationService;

    @BeforeEach
    public void setup() {
        repository = Mockito.mock(UserRepository.class);
        passwordEncoder = Mockito.mock(PasswordEncoder.class);
        registrationService = new RegistrationServiceImpl(repository, passwordEncoder);
    }

    @Test
    public void testRegister_Success() throws Exception {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .name("chrisen")
                .password("password")
                .username("chrisen").build();
        Optional<List<User>> optional = Optional.of(Collections.EMPTY_LIST);
        User user = User.builder()
                .id(1)
                .username("chrisen")
                .password("password")
                .name("chrisen").build();

        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("password");
        when(repository.findByUsername(anyString())).thenReturn(optional);
        when(repository.saveAndFlush(any(User.class))).thenReturn(user);

        registrationService.register(registrationRequest);

        verify(repository, times(1)).saveAndFlush(any(User.class));
    }

    @Test
    public void testRegisterDuplicateUsername_Fail() throws Exception {
        RegistrationRequest registrationRequest = RegistrationRequest.builder()
                .name("chrisen")
                .password("password")
                .username("chrisen").build();
        User user = User.builder()
                .id(1)
                .username("chrisen")
                .password("password")
                .name("chrisen").build();
        Optional<List<User>> optional = Optional.of(Collections.singletonList(user));
        when(repository.findByUsername(anyString())).thenReturn(optional);

        assertThrows(
                RegistrationDuplicateException.class, () -> registrationService.register(registrationRequest));

    }
}
