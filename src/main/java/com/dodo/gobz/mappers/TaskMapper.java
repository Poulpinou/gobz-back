package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.RunTask;
import com.dodo.gobz.models.Task;
import com.dodo.gobz.payloads.dto.ProjectMemberDto;
import com.dodo.gobz.payloads.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ModelMapper mapper;

    private final ProjectMemberMapper projectMemberMapper;

    public TaskDto mapToDto(Task task) {
        return mapToDto(task, false);
    }

    public TaskDto mapToDto(Task task, boolean withWorkers) {
        final TaskDto dto = mapper.map(task, TaskDto.class);

        if (withWorkers) {
            final List<ProjectMemberDto> workers = task.getRuns()
                    .stream()
                    .map(RunTask::getRun)
                    .map(Run::getMember)
                    .map(projectMemberMapper::mapToDto)
                    .collect(Collectors.toList());

            dto.setWorkers(workers);
        }

        return dto;
    }
}