package ru.authservice.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Сущность пользователя. Хранится в базе данных.
 */
@Entity
@Table(name = "users")
@Data
public class User {
    @Id
    @GeneratedValue
    private UUID id; // Уникальный идентификатор пользователя

    @NotBlank
    @Column(nullable = false, unique = true)
    private String username; // Логин пользователя

    @NotBlank
    @Column(nullable = false)
    private String password; // Хэш пароля

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email; // Электронная почта

    @ManyToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();
}
