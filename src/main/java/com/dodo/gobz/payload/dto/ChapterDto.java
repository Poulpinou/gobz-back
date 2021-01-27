package com.dodo.gobz.payload.dto;

import lombok.Data;

import java.util.List;

@Data
public class ChapterDto {
    private Long id;
    private String name;
    private String description;
    private List<StepDto> steps;
}
