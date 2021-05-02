package com.dodo.gobz.mappers;

import com.dodo.gobz.models.ProjectMember;
import com.dodo.gobz.models.User;
import com.dodo.gobz.payloads.dto.ProjectMemberDto;
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
