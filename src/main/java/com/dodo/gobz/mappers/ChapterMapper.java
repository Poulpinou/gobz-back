package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Chapter;
import com.dodo.gobz.payloads.dto.ChapterDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
@RequiredArgsConstructor
public class ChapterMapper {

    private final ModelMapper mapper;

    @PostConstruct
    private void configureMapper() {
        mapper.getConfiguration().setAmbiguityIgnored(true);
        mapper.addMappings(new PropertyMap<Chapter, ChapterDto>() {
            @Override
            protected void configure() {
                skip().setCompletion(null);
            }
        });
    }

    public ChapterDto mapToDto(Chapter chapter) {
        return mapToDto(chapter, false);
    }

    public ChapterDto mapToDto(Chapter chapter, boolean withCompletion) {
        final ChapterDto dto = mapper.map(chapter, ChapterDto.class);

        if (withCompletion) {
            dto.setCompletion((float) chapter.getCompletion());
        }

        return dto;
    }
}