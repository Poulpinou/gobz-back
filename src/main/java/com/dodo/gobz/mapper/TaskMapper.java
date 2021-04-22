package com.dodo.gobz.mapper;

import com.dodo.gobz.model.Task;
import com.dodo.gobz.payload.dto.TaskDto;
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