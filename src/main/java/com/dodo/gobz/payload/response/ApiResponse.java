package com.dodo.gobz.payload.response;

import lombok.Data;

@Data
public class ApiResponse {
    private final boolean success;
    private final String message;
}
