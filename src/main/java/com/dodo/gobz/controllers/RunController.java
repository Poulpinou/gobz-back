package com.dodo.gobz.controllers;

import com.dodo.gobz.exceptions.BadRequestException;
import com.dodo.gobz.exceptions.ResourceNotFoundException;
import com.dodo.gobz.mappers.RunMapper;
import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.models.Task;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.common.RunStatus;
import com.dodo.gobz.payloads.dto.RunDto;
import com.dodo.gobz.payloads.requests.RunCreationRequest;
import com.dodo.gobz.repositories.RunRepository;
import com.dodo.gobz.repositories.StepRepository;
import com.dodo.gobz.security.CurrentUser;
import com.dodo.gobz.security.UserPrincipal;
import com.dodo.gobz.services.RunService;
import com.dodo.gobz.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@PreAuthorize("hasRole('USER')")
public class RunController {

    private final UserService userService;
    private final RunService runService;

    private final RunRepository runRepository;
    private final StepRepository stepRepository;

    private final RunMapper runMapper;

    @GetMapping("/runs/{runId}")
    @Transactional
    public RunDto getRun(@PathVariable long runId) {
        final Run run = runRepository.findById(runId)
                .map(runService::checkRunStatus)
                .orElseThrow(() -> new ResourceNotFoundException("Run", "runId", runId));

        return runMapper.mapToDto(run);
    }

    @PostMapping("/runs")
    @Transactional
    public RunDto createRun(@CurrentUser UserPrincipal userPrincipal, @Valid @RequestBody RunCreationRequest request) {
        final User user = userService.getUserFromPrincipal(userPrincipal);

        final Step step = stepRepository.findById(request.getStepId())
                .orElseThrow(() -> new ResourceNotFoundException("Step", "stepId", request.getStepId()));

        final List<Task> tasks = step.getTasks()
                .stream()
                .filter(task -> request.getTaskIds().contains(task.getId()))
                .collect(Collectors.toList());

        if (tasks.isEmpty()) {
            throw new BadRequestException("None of provided tasks exist int the step");
        }

        final LocalDate limitDate;
        if (request.getLimitDate() != null) {
            limitDate = request.getLimitDate();

            if (limitDate.isBefore(LocalDate.now())) {
                throw new BadRequestException("Limit date should be after today's date");
            }
        } else {
            limitDate = null;
        }

        final Run run = Run.builder()
                .user(user)
                .step(step)
                .tasks(tasks)
                .status(RunStatus.ACTIVE)
                .limitDate(limitDate)
                .build();

        return runMapper.mapToDto(runRepository.save(run));
    }
}
