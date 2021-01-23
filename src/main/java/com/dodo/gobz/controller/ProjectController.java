package com.dodo.gobz.controller;

import com.dodo.gobz.exception.NotImplementedException;
import com.dodo.gobz.exception.ResourceAccessForbiddenException;
import com.dodo.gobz.exception.ResourceNotFoundException;
import com.dodo.gobz.model.Project;
import com.dodo.gobz.model.ProjectMember;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import com.dodo.gobz.payload.request.ProjectCreationRequest;
import com.dodo.gobz.payload.request.ProjectUpdateRequest;
import com.dodo.gobz.payload.response.ApiResponse;
import com.dodo.gobz.repository.ProjectRepository;
import com.dodo.gobz.repository.UserRepository;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ProjectController {

    private final ProjectService projectService;
    private final UserService userService;

    private final ProjectRepository projectRepository;

    @GetMapping("/projects")
    public List<Project> getProjects(@CurrentUser UserPrincipal userPrincipal) {
        throw new NotImplementedException();
    }

    @GetMapping("/projects/mine")
    public List<Project> getMyProjects(@CurrentUser UserPrincipal userPrincipal) {
        throw new NotImplementedException();
    }

    @GetMapping("/projects/{projectId}")
    public Project getProjectById(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read this project", MemberRole.VIEWER));
        }

        return project;
    }

    @PostMapping("/projects")
    @ResponseStatus(HttpStatus.CREATED)
    @Transactional
    public ResponseEntity<?> createProject(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ProjectCreationRequest request) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        Project project = Project.builder()
                .name(request.getName())
                .members(new ArrayList<>())
                .build();

        project = projectRepository.save(project);

        projectService.addMember(project, user, MemberRole.OWNER);

        final URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath()
                .path("/projects")
                .buildAndExpand(project.getId())
                .toUri();

        return ResponseEntity.created(location)
                .body(new ApiResponse(true, "Project created successfully"));
    }

    @PutMapping("/projects/{projectId}")
    @Transactional
    public Project updateProject(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody ProjectUpdateRequest request, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read this project", MemberRole.CONTRIBUTOR));
        }

        project.setName(request.getName());
        project.setDescription(request.getDescription());

        return projectRepository.save(project);
    }

    @DeleteMapping("/projects/{projectId}")
    @Transactional
    public ResponseEntity<?> deleteProject(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "projectId", projectId));

        if (!projectService.userHasRequiredRole(project, user, MemberRole.OWNER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read this project", MemberRole.OWNER));
        }

        projectRepository.delete(project);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Project deleted successfully"));
    }
}
