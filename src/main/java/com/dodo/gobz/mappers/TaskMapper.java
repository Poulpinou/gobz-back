package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Task;
import com.dodo.gobz.payloads.dto.TaskDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TaskMapper {

    private final ModelMapper mapper;

    public TaskDto mapToDto(Task task) {
        return mapper.map(task, TaskDto.class);
    }
}