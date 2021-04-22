package com.dodo.gobz.payload.request;

import com.dodo.gobz.model.common.TaskType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskUpdateRequest {
    @NotBlank
    private String text;

    private TaskType type;
}
