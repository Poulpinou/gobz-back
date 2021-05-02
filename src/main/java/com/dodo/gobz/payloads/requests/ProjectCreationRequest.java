package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProjectCreationRequest {
    @NotBlank
    private String name;

    private String description;

    private Boolean shared;
}
