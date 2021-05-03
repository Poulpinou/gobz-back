package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Project;
import com.dodo.gobz.payloads.dto.ProgressInfosDto;
import com.dodo.gobz.payloads.dto.ProjectDto;
import com.dodo.gobz.payloads.dto.ProjectInfosDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper mapper;

    public ProjectDto mapToDto(Project project) {
        return mapper.map(project, ProjectDto.class);
    }

    public ProjectInfosDto mapToInfosDto(Project project){
        return ProjectInfosDto.builder()
                .project(mapToDto(project))
                .progressInfos(mapToProgressInfosDto(project))
                .build();
    }

    private ProgressInfosDto mapToProgressInfosDto(Project project){
        final AtomicInteger chapterCount = new AtomicInteger();
        final AtomicInteger stepsCount = new AtomicInteger();
        final AtomicInteger tasksCount = new AtomicInteger();
        final AtomicInteger doneTaskCount = new AtomicInteger();

        project.getChapters()
                .forEach(chapter -> {
                    chapterCount.getAndIncrement();
                    chapter.getSteps()
                            .forEach(step -> {
                                stepsCount.getAndIncrement();
                                step.getTasks()
                                        .forEach(task -> {
                                            tasksCount.getAndIncrement();
                                            if(task.isDone()){
                                                doneTaskCount.getAndIncrement();
                                            }
                                        });
                            });
                });

        return ProgressInfosDto.builder()
                .chaptersAmount(chapterCount.get())
                .stepsAmount(stepsCount.get())
                .tasksAmount(tasksCount.get())
                .tasksDoneAmount(doneTaskCount.get())
                .build();
    }
}
