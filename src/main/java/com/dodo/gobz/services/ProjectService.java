package com.dodo.gobz.services;

import com.dodo.gobz.exceptions.AlreadyAProjectMemberException;
import com.dodo.gobz.exceptions.ResourceAccessForbiddenException;
import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.ProjectMember;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.common.MemberRole;
import com.dodo.gobz.repositories.ProjectMemberRepository;
import com.dodo.gobz.repositories.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

    @Transactional
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

    public List<Project> getAllProjects(User user) {
        return projectRepository.getProjectsByUserId(user.getId());
    }

    public Project getProjectByIdIfAuthorized(long projectId, User user) {
        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!project.isShared() && !userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read this project", MemberRole.VIEWER));
        }

        return project;
    }
}
