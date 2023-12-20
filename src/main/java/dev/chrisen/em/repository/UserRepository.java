package dev.chrisen.em.repository;

import dev.chrisen.em.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<List<User>> findByUsername(String username);
}
