package ru.authservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.authservice.entity.Role;
import ru.authservice.entity.User;
import ru.authservice.repository.RoleRepository;
import ru.authservice.repository.UserRepository;

import java.util.*;

/**
 * Сервис для управления пользователями.
 */
@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Регистрирует нового пользователя.
     */
    public User registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole); // Сохранить новую роль в базу
                });
        user.setRoles(Collections.singleton(userRole));
        return userRepository.save(user);
    }

    /**
     * Находит пользователя по имени.
     */
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    /**
     * Находит пользователя по электронной почте.
     */
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    /**
     * Поиск пользователя по его ID.
     */
    public Optional<User> findById(UUID userId) {
        return userRepository.findById(userId);
    }
}
