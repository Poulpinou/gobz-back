package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskUpdateRequest {
    @NotBlank
    private String text;
}
