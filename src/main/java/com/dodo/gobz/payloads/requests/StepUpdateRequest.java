package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class StepUpdateRequest {
    @NotBlank
    private String name;

    private String description;
}
