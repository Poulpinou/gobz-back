package com.dodo.gobz.payloads.dto;

import com.dodo.gobz.models.enums.RunStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class RunDto {
    private long id;

    private StepDto step;

    private ProjectDto project;

    private ProjectMemberDto owner;

    private List<RunTaskDto> tasks;

    private RunStatus status;

    private boolean hasLimitDate;

    private LocalDate limitDate;

    private double completion;
}
