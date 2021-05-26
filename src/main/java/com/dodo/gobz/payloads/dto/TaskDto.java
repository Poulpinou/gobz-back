package com.dodo.gobz.payloads.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.util.List;

@Data
public class TaskDto {
    private Long id;

    private String text;

    private boolean isDone;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<ProjectMemberDto> workers;
}
