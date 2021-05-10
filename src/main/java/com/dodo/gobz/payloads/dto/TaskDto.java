package com.dodo.gobz.payloads.dto;

import com.dodo.gobz.models.common.TaskType;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String text;
    @JsonProperty("isDone")
    private boolean isDone;
    //private TaskType type;
}
