package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Chapter;
import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.payloads.dto.RunDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RunMapper {

    private final ChapterMapper chapterMapper;
    private final StepMapper stepMapper;
    private final TaskMapper taskMapper;
    private final ProjectMapper projectMapper;

    public RunDto mapToDto(Run run) {

        final Step step = run.getStep();
        final Chapter chapter = step.getChapter();
        final Project project = chapter.getProject();

        return RunDto.builder()
                .id(run.getId())
                .project(projectMapper.mapToDto(project))
                .chapter(chapterMapper.mapToDto(chapter))
                .step(stepMapper.mapToDto(step))
                .tasks(run.getTasks()
                        .stream()
                        .map(taskMapper::mapToDto)
                        .collect(Collectors.toList())
                )
                .status(run.getStatus())
                .hasLimitDate(run.hasLimitDate())
                .limitDate(run.getLimitDate())
                .completion(run.getCompletion())
                .build();
    }
}