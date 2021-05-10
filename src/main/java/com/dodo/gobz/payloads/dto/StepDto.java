package com.dodo.gobz.payloads.dto;

import lombok.Data;

@Data
public class StepDto {
    private Long id;
    private String name;
    private String description;
    private float completion;
}
