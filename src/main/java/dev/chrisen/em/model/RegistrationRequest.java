package dev.chrisen.em.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class RegistrationRequest {
    private String username;
    private String password;
    private String name;
}
