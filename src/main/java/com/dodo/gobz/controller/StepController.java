package com.dodo.gobz.controller;

import com.dodo.gobz.exception.ResourceAccessForbiddenException;
import com.dodo.gobz.exception.ResourceNotFoundException;
import com.dodo.gobz.mapper.StepMapper;
import com.dodo.gobz.model.Chapter;
import com.dodo.gobz.model.Project;
import com.dodo.gobz.model.Step;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import com.dodo.gobz.payload.dto.StepDto;
import com.dodo.gobz.payload.request.StepCreationRequest;
import com.dodo.gobz.payload.request.StepUpdateRequest;
import com.dodo.gobz.payload.response.ApiResponse;
import com.dodo.gobz.repository.ChapterRepository;
import com.dodo.gobz.repository.StepRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.service.ProjectService;
import com.dodo.gobz.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.ArrayList;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class StepController {

    private final ProjectService projectService;
    private final UserService userService;

    private final ChapterRepository chapterRepository;
    private final StepRepository stepRepository;

    private final StepMapper stepMapper;

    @GetMapping("/steps/{stepId}")
    public StepDto getStep(@CurrentUser UserPrincipal userPrincipal, @PathVariable long stepId) {
        final Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", stepId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = step.getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Step", String.format("user should at least have the %s role to read this step", MemberRole.VIEWER));
        }

        return stepMapper.mapToDto(step);
    }

    @PostMapping("/chapters/{chapterId}/steps")
    @Transactional
    public StepDto createStep(@CurrentUser UserPrincipal userPrincipal, @PathVariable long chapterId, @Valid @RequestBody StepCreationRequest request) {
        final Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "chapterId", chapterId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = chapter.getProject();
        if (!projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to create a step for this project", MemberRole.CONTRIBUTOR));
        }

        final Step step = Step.builder()
                .chapter(chapter)
                .name(request.getName())
                .description(request.getDescription())
                .tasks(new ArrayList<>())
                .build();

        return stepMapper.mapToDto(stepRepository.save(step));
    }

    @PutMapping("/steps/{stepId}")
    @Transactional
    public StepDto updateStep(@CurrentUser UserPrincipal userPrincipal, @PathVariable long stepId, @Valid @RequestBody StepUpdateRequest request) {
        final Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", stepId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = step.getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Step", String.format("user should at least have the %s role to edit this step", MemberRole.CONTRIBUTOR));
        }

        step.setName(request.getName());
        step.setDescription(request.getDescription());

        return stepMapper.mapToDto(stepRepository.save(step));
    }

    @DeleteMapping("/steps/{stepId}")
    @Transactional
    public ResponseEntity<ApiResponse> deleteStep(@CurrentUser UserPrincipal userPrincipal, @PathVariable long stepId) {
        final Step step = stepRepository.findById(stepId)
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", stepId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = step.getChapter().getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Step", String.format("user should at least have the %s role to delete this step", MemberRole.CONTRIBUTOR));
        }

        stepRepository.delete(step);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Step deleted successfully"));
    }
}
