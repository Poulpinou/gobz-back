package com.dodo.gobz.payloads.requests;

import com.dodo.gobz.models.common.TaskType;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class TaskUpdateRequest {
    @NotBlank
    private String text;

    private TaskType type;
}
