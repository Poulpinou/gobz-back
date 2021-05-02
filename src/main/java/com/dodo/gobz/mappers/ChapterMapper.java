package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Chapter;
import com.dodo.gobz.payloads.dto.ChapterDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChapterMapper {

    private final ModelMapper mapper;

    public ChapterDto mapToDto(Chapter chapter) {
        return mapper.map(chapter, ChapterDto.class);
    }
}