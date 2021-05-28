package com.dodo.gobz.controllers;

import com.dodo.gobz.exceptions.ResourceAccessForbiddenException;
import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.mappers.StepMapper;
import com.dodo.gobz.models.Chapter;
import com.dodo.gobz.models.Project;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.enums.MemberRole;
import com.dodo.gobz.payloads.dto.StepDto;
import com.dodo.gobz.payloads.requests.StepCreationRequest;
import com.dodo.gobz.payloads.requests.StepUpdateRequest;
import com.dodo.gobz.payloads.responses.ApiResponse;
import com.dodo.gobz.repositories.ChapterRepository;
import com.dodo.gobz.repositories.StepRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.services.ProjectService;
import com.dodo.gobz.services.UserService;
import io.swagger.annotations.Api;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
@Api(
        tags = "Steps"
)
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

        return stepMapper.mapToDto(step, true);
    }

    @GetMapping("/chapters/{chapterId}/steps")
    public List<StepDto> getChapterSteps(@CurrentUser UserPrincipal userPrincipal, @PathVariable long chapterId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "chapterId", chapterId));
        final Project project = chapter.getProject();

        if (!projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to read steps for this project", MemberRole.VIEWER));
        }

        return chapter.getSteps()
                .stream()
                .map(step -> stepMapper.mapToDto(step, true))
                .collect(Collectors.toList());
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
