package com.dodo.gobz.mapper;

import com.dodo.gobz.model.Project;
import com.dodo.gobz.payload.dto.FullProjectDto;
import com.dodo.gobz.payload.dto.ProjectDto;
import com.dodo.gobz.payload.dto.ProjectMemberDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper mapper;
    private final ProjectMemberMapper projectMemberMapper;

    public ProjectDto mapToDto(Project project) {
        return mapper.map(project, ProjectDto.class);
    }

    public FullProjectDto mapToFullDto(Project project) {
        final FullProjectDto dto = mapper.map(project, FullProjectDto.class);
        final List<ProjectMemberDto> memberDtos = project.getMembers()
                .stream()
                .map(projectMemberMapper::mapToDto)
                .collect(Collectors.toList());

        dto.setMembers(memberDtos);

        return dto;
    }
}
