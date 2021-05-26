package com.dodo.gobz.controllers;

import com.dodo.gobz.exceptions.ResourceAccessForbiddenException;
import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.mappers.TaskMapper;
import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.models.Task;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.enums.MemberRole;
import com.dodo.gobz.payloads.dto.TaskDto;
import com.dodo.gobz.payloads.requests.TaskCreationRequest;
import com.dodo.gobz.payloads.requests.TaskUpdateRequest;
import com.dodo.gobz.payloads.responses.ApiResponse;
import com.dodo.gobz.repositories.StepRepository;
import com.dodo.gobz.repositories.TaskRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.services.ProjectService;
import com.dodo.gobz.services.TaskService;
import com.dodo.gobz.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class TaskController {

    private final ProjectService projectService;
    private final UserService userService;
    private final TaskService taskService;

    private final StepRepository stepRepository;
    private final TaskRepository taskRepository;

    private final TaskMapper taskMapper;

    @GetMapping("/tasks/{taskId}")
    public TaskDto getTask(@CurrentUser UserPrincipal userPrincipal, @PathVariable long taskId) {
        final Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = task.getStep().getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Task", String.format("user should at least have the %s role to read this task", MemberRole.VIEWER));
        }

        return taskMapper.mapToDto(task);
    }

    @GetMapping("/steps/{stepId}/tasks")
    public List<TaskDto> getStepTasks(@CurrentUser UserPrincipal userPrincipal, @PathVariable long stepId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("step", "stepId", stepId));
        final Project project = step.getChapter().getProject();

        if (!projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read tasks for this project", MemberRole.VIEWER));
        }

        return step.getTasks()
                .stream()
                .map(taskMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/steps/{stepId}/tasks")
    @Transactional
    public TaskDto createTask(@CurrentUser UserPrincipal userPrincipal, @PathVariable long stepId, @Valid @RequestBody TaskCreationRequest request) {
        final Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", stepId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = step.getChapter().getProject();
        if (!projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to create a task for this project", MemberRole.CONTRIBUTOR));
        }

        final Task task = Task.builder()
                .step(step)
                .text(request.getText())
                .done(false)
                .build();

        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @PutMapping("/tasks/{taskId}")
    @Transactional
    public TaskDto updateTask(@CurrentUser UserPrincipal userPrincipal, @PathVariable long taskId, @Valid @RequestBody TaskUpdateRequest request) {
        final Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = task.getStep().getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Task", String.format("user should at least have the %s role to edit this task", MemberRole.CONTRIBUTOR));
        }

        task.setText(request.getText());

        return taskMapper.mapToDto(taskRepository.save(task));
    }

    @DeleteMapping("/tasks/{taskId}")
    @Transactional
    public ResponseEntity<ApiResponse> deleteTask(@CurrentUser UserPrincipal userPrincipal, @PathVariable long taskId) {
        final Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = task.getStep().getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Task", String.format("user should at least have the %s role to delete this task", MemberRole.CONTRIBUTOR));
        }

        taskRepository.delete(task);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Task deleted successfully"));
    }

    @PatchMapping("/tasks/{taskId}/finish")
    @Transactional
    public Task finishTask(@CurrentUser UserPrincipal userPrincipal, @PathVariable long taskId) {
        final Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "taskId", taskId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = task.getStep().getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Task", String.format("user should at least have the %s role to finish this task", MemberRole.CONTRIBUTOR));
        }

        return taskService.updateTaskStatus(task, true);
    }
}
