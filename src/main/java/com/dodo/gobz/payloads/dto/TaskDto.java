package com.dodo.gobz.payloads.dto;

import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long id;

    private String text;

    private boolean isDone;

    private List<ProjectMemberDto> workers;
}
