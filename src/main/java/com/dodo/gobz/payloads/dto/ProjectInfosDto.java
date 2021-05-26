package com.dodo.gobz.payloads.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProjectInfosDto {

    private ProjectDto project;

    private ProgressInfosDto progressInfos;
}
