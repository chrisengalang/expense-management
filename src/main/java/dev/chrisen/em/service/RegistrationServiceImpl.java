package dev.chrisen.em.service;

import dev.chrisen.em.exception.RegistrationDuplicateException;
import dev.chrisen.em.model.RegistrationRequest;
import dev.chrisen.em.model.User;
import dev.chrisen.em.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static dev.chrisen.em.constant.ResponseMessageConstants.REGISTRATION_DUPLICATE_USERNAME;

@Component
@RequiredArgsConstructor
public class RegistrationServiceImpl implements RegistrationService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegistrationRequest request) {
        if (isDuplicate(request.getUsername())) {
            throw new RegistrationDuplicateException(REGISTRATION_DUPLICATE_USERNAME);
        } else {
            User user = User.builder()
                    .name(request.getName())
                    .username(request.getUsername())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .build();

            userRepository.saveAndFlush(user);
        }
    }

    private boolean isDuplicate(String username) {
        return !userRepository.findByUsername(username).get().isEmpty();
    }
}
