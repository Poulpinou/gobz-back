package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Step;
import com.dodo.gobz.payloads.dto.StepDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class StepMapper {

    private final ModelMapper mapper;

    @PostConstruct
    private void configureMapper() {
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.addMappings(new PropertyMap<Step, StepDto>() {
            @Override
            protected void configure() {
                skip().setCompletion(null);
            }
        });
    }

    public StepDto mapToDto(Step step) {
        return mapToDto(step, false);
    }

    public StepDto mapToDto(Step step, boolean withCompletion) {
        final StepDto dto = mapper.map(step, StepDto.class);

        if (withCompletion) {
            dto.setCompletion((float) step.getCompletion());
        }

        return dto;
    }
}