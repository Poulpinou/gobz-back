package com.dodo.gobz.payload.dto;

import com.dodo.gobz.model.common.TaskType;
import lombok.Data;

@Data
public class TaskDto {
    private Long id;
    private String text;
    private boolean isDone;
    private TaskType type;
}
