package com.dodo.gobz.payloads.responses;

import lombok.Data;

@Data
public class ApiResponse {
    private final boolean success;
    private final String message;
}
