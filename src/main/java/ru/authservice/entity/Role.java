package ru.authservice.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Set;
import java.util.UUID;

/**
 * Сущность роли пользователя.
 */
@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue
    private UUID id; // Уникальный идентификатор роли

    @Column(nullable = false, unique = true)
    private String name; // Название роли (например, ROLE_USER, ROLE_ADMIN)

    @ManyToMany(mappedBy = "roles")
    private Set<User> users; // Пользователи с этой ролью
}
