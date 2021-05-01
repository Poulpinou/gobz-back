package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Step;
import com.dodo.gobz.payloads.dto.StepDto;
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