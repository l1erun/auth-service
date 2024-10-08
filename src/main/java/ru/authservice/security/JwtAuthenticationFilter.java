package ru.authservice.security;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.*;
import org.springframework.security.core.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import ru.authservice.dto.LoginRequest;
import ru.authservice.dto.JwtResponse;
import ru.authservice.service.JwtService;

import java.io.IOException;

/**
 * Фильтр для аутентификации пользователей и выдачи JWT токенов.
 */
public class JwtAuthenticationFilter {
//public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//    private AuthenticationManager authenticationManager;
//    private JwtService jwtService;
//
//    public JwtAuthenticationFilter(AuthenticationManager authenticationManager, JwtService jwtService) {
//        this.authenticationManager = authenticationManager;
//        this.jwtService = jwtService;
//        setFilterProcessesUrl("/auth/login");
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
//            throws AuthenticationException {
//
//        try {
//            ObjectMapper mapper = new ObjectMapper();
//            LoginRequest loginRequest = mapper.readValue(request.getInputStream(), LoginRequest.class);
//
//            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
//                    loginRequest.getUsername(),
//                    loginRequest.getPassword()
//            );
//
//            return authenticationManager.authenticate(authToken);
//
//        } catch (IOException e) {
//            throw new AuthenticationServiceException("Invalid login request", e);
//        }
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
//                                            jakarta.servlet.FilterChain chain, Authentication authResult)
//            throws IOException {
//        UserDetails userDetails = (UserDetails) authResult.getPrincipal(); // Получаем UserDetails
//        String accessToken = jwtService.generateTokenFromUserDetails(userDetails); // Генерация access token
//        String refreshToken = jwtService.generateRefreshTokenFromUserDetails(userDetails); // Генерация refresh token
//        System.out.println(userDetails);
//        // Возвращаем оба токена
//        JwtResponse jwtResponse = new JwtResponse(accessToken, refreshToken);
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//        response.getWriter().write(new ObjectMapper().writeValueAsString(jwtResponse));
//    }
}
