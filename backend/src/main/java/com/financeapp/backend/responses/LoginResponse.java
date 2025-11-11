package com.financeapp.backend.responses;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class LoginResponse {
    private String token;
    private long expiresIn;
    private String refreshToken;

    public LoginResponse(String token, long expiresIn, String refreshToken) {
        this.token = token;
        this.expiresIn = expiresIn;
        this.refreshToken = refreshToken;
    }
}
