package com.dodo.gobz.payloads.dto;

import lombok.Data;

@Data
public class ChapterDto {
    private Long id;
    private String name;
    private String description;
    private float completion;
}