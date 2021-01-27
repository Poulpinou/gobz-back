package com.dodo.gobz.payload.dto;

import lombok.Data;

import java.util.List;

@Data
public class StepDto {
    private Long id;
    private String name;
    private String description;
    private List<TaskDto> tasks;
}
