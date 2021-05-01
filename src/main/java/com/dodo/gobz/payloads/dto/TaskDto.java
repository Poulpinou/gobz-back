package com.dodo.gobz.payloads.dto;

import com.dodo.gobz.models.common.TaskType;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String text;
    private boolean isDone;
    private TaskType type;
}
