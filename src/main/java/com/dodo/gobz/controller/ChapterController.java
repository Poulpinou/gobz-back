package com.dodo.gobz.controller;

import com.dodo.gobz.exception.ResourceAccessForbiddenException;
import com.dodo.gobz.exception.ResourceNotFoundException;
import com.dodo.gobz.mapper.ChapterMapper;
import com.dodo.gobz.model.Chapter;
import com.dodo.gobz.model.Project;
import com.dodo.gobz.model.User;
import com.dodo.gobz.model.common.MemberRole;
import com.dodo.gobz.payload.dto.ChapterDto;
import com.dodo.gobz.payload.request.ChapterCreationRequest;
import com.dodo.gobz.payload.request.ChapterUpdateRequest;
import com.dodo.gobz.payload.response.ApiResponse;
import com.dodo.gobz.repository.ChapterRepository;
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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class ChapterController {

    private final ProjectService projectService;
    private final UserService userService;

    private final ChapterRepository chapterRepository;

    private final ChapterMapper chapterMapper;

    @GetMapping("/projects/{projectId}/chapters")
    public List<ChapterDto> getProjectChapters(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId) {
        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = projectService.getProjectByIdIfAuthorized(projectId, user);

        return project.getChapters()
                .stream()
                .map(chapterMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/chapters/{chapterId}")
    public ChapterDto getChapter(@CurrentUser UserPrincipal userPrincipal, @PathVariable long chapterId) {
        final Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "chapterId", chapterId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = chapter.getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.VIEWER)) {
            throw new ResourceAccessForbiddenException("Chapter", String.format("user should at least have the %s role to read this chapter", MemberRole.VIEWER));
        }

        return chapterMapper.mapToDto(chapter);
    }

    @PostMapping("/projects/{projectId}/chapters")
    @Transactional
    public ChapterDto createChapter(@CurrentUser UserPrincipal userPrincipal, @PathVariable long projectId, @Valid @RequestBody ChapterCreationRequest request) {
        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = projectService.getProjectByIdIfAuthorized(projectId, user);

        if (!projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Project", String.format("user should at least have the %s role to create a chapter for this project", MemberRole.CONTRIBUTOR));
        }

        final Chapter chapter = Chapter.builder()
                .project(project)
                .name(request.getName())
                .description(request.getDescription())
                .steps(new ArrayList<>())
                .build();

        return chapterMapper.mapToDto(chapterRepository.save(chapter));
    }

    @PutMapping("/chapters/{chapterId}")
    @Transactional
    public ChapterDto updateChapter(@CurrentUser UserPrincipal userPrincipal, @PathVariable long chapterId, @Valid @RequestBody ChapterUpdateRequest request) {
        final Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "chapterId", chapterId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = chapter.getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Chapter", String.format("user should at least have the %s role to edit this chapter", MemberRole.CONTRIBUTOR));
        }

        chapter.setName(request.getName());
        chapter.setDescription(request.getDescription());

        return chapterMapper.mapToDto(chapterRepository.save(chapter));
    }

    @DeleteMapping("/chapters/{chapterId}")
    @Transactional
    public ResponseEntity<ApiResponse> deleteChapter(@CurrentUser UserPrincipal userPrincipal, @PathVariable long chapterId) {
        final Chapter chapter = chapterRepository.findById(chapterId)
                .orElseThrow(() -> new ResourceNotFoundException("Chapter", "chapterId", chapterId));

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Project project = chapter.getProject();
        if (!project.isShared() && !projectService.userHasRequiredRole(project, user, MemberRole.CONTRIBUTOR)) {
            throw new ResourceAccessForbiddenException("Chapter", String.format("user should at least have the %s role to delete this chapter", MemberRole.CONTRIBUTOR));
        }

        chapterRepository.delete(chapter);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Chapter deleted successfully"));
    }
}
