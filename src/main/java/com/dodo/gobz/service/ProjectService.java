package com.dodo.gobz.service;

import com.dodo.gobz.exception.AlreadyAProjectMemberException;
import com.dodo.gobz.exception.ResourceAccessForbiddenException;
import com.dodo.gobz.model.Project;
import com.dodo.gobz.model.ProjectMember;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import com.dodo.gobz.repository.ProjectMemberRepository;
import com.dodo.gobz.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProjectService {

    private final ProjectRepository projectRepository;

    private final ProjectMemberRepository projectMemberRepository;

    public boolean userIsProjectMember(Project project, User user) {
        return project.getMembers()
                .stream()
                .anyMatch(projectMember -> projectMember.getUser() == user);
    }

    public Optional<ProjectMember> getUserMembership(Project project, User user) {
        return project.getMembers()
                .stream()
                .filter(projectMember -> projectMember.getUser() == user)
                .findFirst();
    }

    public boolean userHasRequiredRole(Project project, User user, MemberRole role) {
        final ProjectMember projectMember = getUserMembership(project, user)
                .orElseThrow(() -> new ResourceAccessForbiddenException("ProjectMember", "user is not a member of this project"));

        return projectMember.getRole().getRolePower() >= role.getRolePower();
    }

    public void addMember(Project project, User member, MemberRole role) {
        if (userIsProjectMember(project, member)) {
            throw new AlreadyAProjectMemberException(project, member);
        }

        final ProjectMember newMember = ProjectMember.builder()
                .project(project)
                .user(member)
                .role(role)
                .build();

        projectMemberRepository.save(newMember);
    }
}
