package com.dodo.gobz.payload.response;

import lombok.Data;

@Data
public class AuthResponse {
    private final String accessToken;
    private String tokenType = "Bearer";
}
