package com.dodo.gobz.mapper;

import com.dodo.gobz.model.Chapter;
import com.dodo.gobz.payload.dto.ChapterDto;
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