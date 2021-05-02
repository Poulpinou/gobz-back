package com.dodo.gobz.mappers;

import com.dodo.gobz.models.Project;
import com.dodo.gobz.payloads.dto.ProjectDto;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ProjectMapper {

    private final ModelMapper mapper;
    private final ProjectMemberMapper projectMemberMapper;

    public ProjectDto mapToDto(Project project) {
        return mapper.map(project, ProjectDto.class);
    }

    /*public FullProjectDto mapToFullDto(Project project) {
        final FullProjectDto dto = mapper.map(project, FullProjectDto.class);
        final List<ProjectMemberDto> memberDtos = project.getMembers()
                .stream()
                .map(projectMemberMapper::mapToDto)
                .collect(Collectors.toList());

        dto.setMembers(memberDtos);

        return dto;
    }*/
}
