package com.dodo.gobz.controllers;

import com.dodo.gobz.configs.AppConfig;
import com.dodo.gobz.exceptions.BadRequestException;
import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.mappers.RunMapper;
import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.enums.RunStatus;
import com.dodo.gobz.payloads.dto.RunDto;
import com.dodo.gobz.payloads.requests.RunCreationRequest;
import com.dodo.gobz.payloads.responses.ApiResponse;
import com.dodo.gobz.repositories.RunRepository;
import com.dodo.gobz.repositories.StepRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.services.ProjectService;
import com.dodo.gobz.services.RunService;
import com.dodo.gobz.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class RunController {

    private final AppConfig appConfig;

    private final UserService userService;
    private final ProjectService projectService;
    private final RunService runService;

    private final RunRepository runRepository;
    private final StepRepository stepRepository;

    private final RunMapper runMapper;

    @GetMapping("/runs/{runId}")
    public RunDto getRun(@PathVariable long runId) {
        final Run run = runRepository.findById(runId)
                .map(runService::checkRunStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Run", "runId", runId));

        return runMapper.mapToDto(run);
    }

    @GetMapping("/runs")
    public List<RunDto> getRuns(@CurrentUser UserPrincipal userPrincipal) {
        return runRepository.getRunsByMemberUserId(userPrincipal.getId())
                .stream()
                .map(runMapper::mapToDto)
                .collect(Collectors.toList());
    }

    @PostMapping("/runs")
    @Transactional
    public RunDto createRun(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody RunCreationRequest request) {
        final int activeRunsCount = runRepository.countRunsByActiveIsTrueAndMemberUserId(userPrincipal.getId());
        if (activeRunsCount >= appConfig.getRun().getMaxActiveAmount()) {
            throw new BadRequestException("Can't create a new run: max active run amount has been reached");
        }

        final User user = userService.getUserFromPrincipal(userPrincipal);
        final Step step = stepRepository.findById(request.getStepId())
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", request.getStepId()));

        final Run run = runService.createRunFromRequest(user, step, request);

        return runMapper.mapToDto(run);
    }

    @PatchMapping("/runs/{runId}/abandon")
    @Transactional
    public ResponseEntity<ApiResponse> abandonRun(@PathVariable long runId) {
        final Run run = runRepository.findById(runId)
                .map(runService::checkRunStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Run", "runId", runId));

        if (run.getStatus() != RunStatus.LATE) {
            throw new BadRequestException(String.format("Can't abandon a run with the %s status", run.getStatus()));
        }

        run.setStatus(RunStatus.ABANDONED);

        return ResponseEntity.ok()
                .body(new ApiResponse(true, "Run abandoned successfully"));
    }

    @GetMapping("/runs/maxActiveAmount")
    public Integer getMaxActiveAmount() {
        return appConfig.getRun().getMaxActiveAmount();
    }
}
