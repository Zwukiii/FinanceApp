package com.financeapp.backend.DTO.jwt;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter

public class TokenRefreshDTO {
    private String refreshToken;
    private String accessToken;
    private Long expiresIn;
}
