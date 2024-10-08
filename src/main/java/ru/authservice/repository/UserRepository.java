package ru.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.authservice.entity.User;

import java.util.Optional;
import java.util.UUID;

/**
 * Репозиторий для работы с пользователями.
 */
@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByUsername(String username);
    Optional<User> findByEmail(String email);
}
