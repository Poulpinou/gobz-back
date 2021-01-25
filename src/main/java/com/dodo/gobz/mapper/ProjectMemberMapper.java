package com.dodo.gobz.mapper;

import com.dodo.gobz.model.ProjectMember;
import com.dodo.gobz.model.User;
import com.dodo.gobz.payload.dto.ProjectMemberDto;
import org.springframework.stereotype.Component;

@Component
public class ProjectMemberMapper {
    public ProjectMemberDto mapToDto(ProjectMember member) {
        final User user = member.getUser();

        return ProjectMemberDto.builder()
                .id(user.getId())
                .name(user.getName())
                .imageUrl(user.getImageUrl())
                .role(member.getRole())
                .build();
    }
}
