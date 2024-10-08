package ru.authservice.dto;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class JwtResponse {

    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private UserResponse userResponse;

    public JwtResponse(String token, String refreshToken) {
        this.token = token;
        this.refreshToken = refreshToken;
    }

    public JwtResponse(String token, String refreshToken, UserResponse userResponse) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.userResponse = userResponse;
    }
}
