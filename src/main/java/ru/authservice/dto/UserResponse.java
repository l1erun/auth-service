package ru.authservice.dto;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import ru.authservice.entity.Role;
import ru.authservice.entity.User;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class UserResponse {
    private UUID id; // Уникальный идентификатор пользователя
    private String username; // Логин пользователя
    private String email; // Электронная почта
    private Set<Role> roles;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.email = user.getEmail();
        this.roles = user.getRoles();
    }
}
