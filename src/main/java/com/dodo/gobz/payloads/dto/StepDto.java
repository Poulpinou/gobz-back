package com.dodo.gobz.payloads.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
public class StepDto {
    private Long id;
    private String name;
    private String description;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Float completion;
}
