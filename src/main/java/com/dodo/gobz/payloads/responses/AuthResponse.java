package com.dodo.gobz.payloads.responses;

import lombok.Data;

@Data
public class AuthResponse {
    private final String accessToken;
    private String tokenType = "Bearer";
}
