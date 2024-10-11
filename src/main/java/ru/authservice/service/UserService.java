package ru.authservice.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import ru.authservice.dto.UserRequest;
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

    @Autowired
    private JwtService jwtService;

    @Autowired
    private WebClient webClient;

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    /**
     * Регистрирует нового пользователя.
     */
    public User registerUser(UserRequest userRequest) {
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setUsername(userRequest.getUsername());
        user.setEmail(userRequest.getEmail());
        user.setPassword(passwordEncoder.encode(userRequest.getPassword()));
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    return roleRepository.save(newRole); // Сохранить новую роль в базу
                });
        user.setRoles(Collections.singleton(userRole));
        createProfileInProfileService(user);
        User newUser = userRepository.save(user);
        logger.info(newUser.toString());
        return newUser;
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

    private void createProfileInProfileService(User user) {
        Map<String, Object> profileData = new HashMap<>();
        String jwtToken = jwtService.generateToken(user);
        profileData.put("username", user.getUsername());
//        profileData.put("email", user.getEmail());

        webClient.post()
                .uri("http://localhost:8080/players/" + user.getId()) // URL микросервиса профиля
                .header("Authorization", "Bearer " + jwtToken) // Добавляем JWT токен в заголовок
                .bodyValue(profileData)
                .retrieve()
                .bodyToMono(Void.class)
                .block(); // Блокирующий вызов
    }
}
