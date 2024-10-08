package ru.authservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.authservice.entity.Role;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Boolean existsByName(String role);
    Optional<Role> findByName(String role);

}
