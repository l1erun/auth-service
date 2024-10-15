package ru.authservice.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.web.bind.annotation.*;
import ru.authservice.dto.JwtResponse;
import ru.authservice.dto.LoginRequest;
import ru.authservice.dto.UserRequest;
import ru.authservice.dto.UserResponse;
import ru.authservice.entity.CustomUserDetails;
import ru.authservice.entity.User;
import ru.authservice.service.JwtService;
import ru.authservice.service.UserService;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

/**
 * Контроллер для аутентификации и регистрации пользователей.
 */
//@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserRequest user) {
        if (userService.findByUsername(user.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("Username is already taken");
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest().body("Email is already in use");
        }
        userService.registerUser(user);
        return ResponseEntity.ok("User registered successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String jwt = jwtService.generateToken(user); // Генерация access token
            String refreshToken = jwtService.generateRefreshToken(user); // Генерация refresh token

            UserResponse userResponse = new UserResponse(user);
            JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, userResponse);
            return ResponseEntity.ok()
                    .body(jwtResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/login/service")
    public ResponseEntity<?> authenticateService(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            User user = userDetails.getUser();
            String jwt = jwtService.generateToken(user); // Генерация access token
            String refreshToken = jwtService.generateRefreshToken(user); // Генерация refresh token

//            UserResponse userResponse = new UserResponse(user);
            JwtResponse jwtResponse = new JwtResponse(jwt, refreshToken, null);
            return ResponseEntity.ok()
                    .body(jwtResponse);
        } catch (AuthenticationException e) {
            return ResponseEntity.status(401).body("Invalid username or password");
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<?> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        // Проверка валидности refresh токена
        if (jwtService.validateToken(refreshToken) && !jwtService.isTokenExpired(refreshToken)) {
            UUID userId = jwtService.getUserIdFromToken(refreshToken);

            // Получаем пользователя по ID
            Optional<User> user = userService.findById(userId);

            if (user.isPresent()) {
                // Генерируем новый access токен
                String newAccessToken = jwtService.generateToken(user.get());

                // Возвращаем новый токен
                return ResponseEntity.ok(new JwtResponse(newAccessToken, refreshToken));
            } else {
                return ResponseEntity.status(401).body("Invalid user");
            }
        } else {
            return ResponseEntity.status(401).body("Invalid or expired refresh token");
        }
    }
}
