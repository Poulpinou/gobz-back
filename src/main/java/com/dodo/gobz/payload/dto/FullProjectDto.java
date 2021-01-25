package com.dodo.gobz.payload.dto;

import lombok.Data;

import java.util.List;

@Data
public class FullProjectDto {
    private long id;
    private String name;
    private String description;
    private boolean shared;
    private List<ProjectMemberDto> members;
}
