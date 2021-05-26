package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Chapter;
import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.payloads.dto.RunDto;
import com.dodo.gobz.payloads.dto.RunTaskDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RunMapper {

    private final StepMapper stepMapper;
    private final ProjectMapper projectMapper;
    private final ProjectMemberMapper projectMemberMapper;

    public RunDto mapToDto(Run run) {

        final Step step = run.getStep();
        final Chapter chapter = step.getChapter();
        final Project project = chapter.getProject();

        return RunDto.builder()
                .id(run.getId())
                .project(projectMapper.mapToDto(project))
                .step(stepMapper.mapToDto(step))
                .tasks(run.getTasks()
                        .stream()
                        .map(runTask -> RunTaskDto.builder()
                                .id(runTask.getTask().getId())
                                .text(runTask.getTask().getText())
                                .isDone(runTask.getTask().isDone())
                                .isAbandoned(runTask.isAbandoned())
                                .build())
                        .collect(Collectors.toList())
                )
                .owner(projectMemberMapper.mapToDto(run.getMember()))
                .status(run.getStatus())
                .hasLimitDate(run.hasLimitDate())
                .limitDate(run.getLimitDate())
                .completion(run.getCompletion())
                .build();
    }
}