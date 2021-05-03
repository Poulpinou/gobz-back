package com.dodo.gobz.payloads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProgressInfosDto {
    private int chaptersAmount;
    private int stepsAmount;
    private int tasksAmount;
    private int tasksDoneAmount;
}
