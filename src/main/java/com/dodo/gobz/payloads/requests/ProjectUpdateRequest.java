package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProjectUpdateRequest {
    @NotBlank
    private String name;

    private String description;

    private boolean isShared;
}
