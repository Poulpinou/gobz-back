package com.dodo.gobz.payload.request;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class ProjectUpdateRequest {
    @NotBlank
    private String name;

    private String description;

    private Boolean shared;
}
