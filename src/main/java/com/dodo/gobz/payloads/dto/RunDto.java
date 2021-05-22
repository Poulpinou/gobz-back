package com.dodo.gobz.payloads.dto;

import com.dodo.gobz.models.common.RunStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
public class RunDto {
    private long id;
    private StepDto step;
    private ChapterDto chapter;
    private ProjectDto project;
    private List<TaskDto> tasks;
    private RunStatus status;
    private boolean hasLimitDate;
    private LocalDate limitDate;
    private double completion;
}
