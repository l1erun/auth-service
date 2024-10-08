package ru.authservice.service;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

import ru.authservice.entity.User;

/**
 * Сервис для работы с JWT.
 */
@Service
public class JwtService {
    @Value("${jwt.secret}")
    private String secretKey;

    @Value("${jwt.expiration}")
    private long expiration;

    @Value("${jwt.refreshExpiration}")
    private long refreshTokenExpiration;

    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secretKey));
    }

    public String generateTokenFromUserDetails(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .collect(Collectors.toList()));

        return Jwts.builder()
                .setSubject(userDetails.getUsername())
                .addClaims(claims)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    // Метод для генерации refresh токена
    public String generateRefreshTokenFromUserDetails(UserDetails userDetails) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(userDetails.getUsername()) // Используем username как уникальный идентификатор
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Генерирует JWT-токен для пользователя.
     */
    public String generateToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + expiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .claim("roles", user.getRoles())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS256, getSigningKey())
                .compact();
    }

    // Генерация refresh токена
    public String generateRefreshToken(User user) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + refreshTokenExpiration);

        return Jwts.builder()
                .setSubject(user.getId().toString())
                .setIssuedAt(now)
                .setExpiration(expiryDate)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Получает идентификатор пользователя из токена.
     */
    public UUID getUserIdFromToken(String token) {
        Claims claims = Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody();

        return UUID.fromString(claims.getSubject());
    }

    /**
     * Проверяет валидность токена.
     */
    public boolean validateToken(String token) {
        try {
            Jwts.parser().setSigningKey(Base64.getDecoder().decode(secretKey)).parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            // Логирование ошибки (можно добавить)
            return false;
        }
    }

    /**
     * Проверяет, не истек ли токен.
     */
    public boolean isTokenExpired(String token) {
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims.getExpiration().before(new Date());
        } catch (JwtException | IllegalArgumentException e) {
            return true; // Если ошибка в токене, считаем его истекшим
        }
    }
}
