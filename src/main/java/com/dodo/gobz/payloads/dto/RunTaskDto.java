package com.dodo.gobz.payloads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RunTaskDto {
    private Long id;

    private String text;

    private boolean isDone;

    private boolean isAbandoned;
}
