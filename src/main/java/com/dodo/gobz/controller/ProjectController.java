package com.dodo.gobz.controller;

import com.dodo.gobz.exception.ResourceAccessForbiddenException;
import com.dodo.gobz.exception.ResourceNotFoundException;
import com.dodo.gobz.mapper.ProjectMapper;
import com.dodo.gobz.model.Project;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import com.dodo.gobz.payload.dto.FullProjectDto;
import com.dodo.gobz.payload.dto.ProjectDto;
import com.dodo.gobz.payload.request.ProjectCreationRequest;
import com.dodo.gobz.payload.request.ProjectUpdateRequest;
import com.dodo.gobz.payload.response.ApiResponse;
import com.dodo.gobz.repository.ProjectRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.service.ProjectService;
import com.dodo.gobz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    private final ProjectRepository projectRepository;

    private final ProjectMapper projectMapper;

    @GetMapping("/projects")
    public List<ProjectDto> getProjects(@CurrentUser UserPrincipal userPrincipal) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        return projectService.getAllProjects(user)
                .stream()
                .map(projectMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/projects/{projectId}")
    public ProjectDto getProjectById(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = projectService.getProjectByIdIfAuthorized(projectId, user);

        return projectMapper.mapToDto(project);
    }

    @GetMapping("/projects/{projectId}/full")
    public FullProjectDto getFullProjectById(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read this project", MemberRole.VIEWER));
        }

        return projectMapper.mapToFullDto(project);
    }

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ProjectDto createProject(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ProjectCreationRequest request) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        Project project = Project.builder()
                .name(request.getName())
                .description(request.getDescription())
                .shared(request.getShared())
                .members(new ArrayList<>())
                .build();

        project = projectRepository.save(project);

        projectService.addMember(project, user, MemberRole.OWNER);

        return projectMapper.mapToDto(project);
    }

    @PutMapping("/projects/{projectId}")
    @Transactional
    public ProjectDto updateProject(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ProjectUpdateRequest request, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!projectService.userHasRequiredRole(project, user, MemberRole.OWNER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to update this project", MemberRole.CONTRIBUTOR));
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setShared(request.getShared());

        return projectMapper.mapToDto(projectRepository.save(project));
    }

    @DeleteMapping("/projects/{projectId}")
    @Transactional
    public ResponseEntity<ApiResponse> deleteProject(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!projectService.userHasRequiredRole(project, user, MemberRole.OWNER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to delete this project", MemberRole.OWNER));
        }

        projectRepository.delete(project);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Project deleted successfully"));
    }
}
