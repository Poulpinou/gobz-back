package com.dodo.gobz.payloads.requests;

import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Data
public class RunCreationRequest {
    @NotNull
    private Long stepId;

    @NotEmpty
    private List<Long> taskIds;

    private LocalDate limitDate;
}
