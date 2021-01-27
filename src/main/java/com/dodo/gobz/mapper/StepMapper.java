package com.dodo.gobz.mapper;

import com.dodo.gobz.model.Step;
import com.dodo.gobz.payload.dto.StepDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class StepMapper {

    private final ModelMapper mapper;

    public StepDto mapToDto(Step step) {
        return mapper.map(step, StepDto.class);
    }
}