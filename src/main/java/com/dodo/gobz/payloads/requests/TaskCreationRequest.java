package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskCreationRequest {
    @NotBlank
    private String text;
}
