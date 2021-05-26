package com.dodo.gobz.services;

import com.dodo.gobz.exceptions.BadRequestException;
import com.dodo.gobz.models.ProjectMember;
import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.RunTask;
import com.dodo.gobz.models.Step;
import com.dodo.gobz.models.Task;
import com.dodo.gobz.models.User;
import com.dodo.gobz.models.enums.RunStatus;
import com.dodo.gobz.payloads.requests.RunCreationRequest;
import com.dodo.gobz.repositories.RunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RunService {

    private final ProjectService projectService;

    private final RunRepository runRepository;

    public Run checkRunStatus(Run run) {
        return checkRunStatus(run, true);
    }

    public Run checkRunStatus(Run run, boolean saveIfStatusChanged) {
        RunStatus status = run.getStatus();

        if (status == RunStatus.DONE) {
            return run;
        }

        // check if all tasks are done
        final boolean allTasksDone = run.getTasks()
                .stream()
                .map(RunTask::getTask)
                .allMatch(Task::isDone);

        if (allTasksDone) {
            status = RunStatus.DONE;
        } else {
            final LocalDate now = LocalDate.now();
            final LocalDate limitDate = run.hasLimitDate()
                    ? run.getLimitDate()
                    : run.getCreatedAt().plusMonths(6).toLocalDate();

            if (now.isAfter(limitDate)) {
                status = RunStatus.LATE;
            }
        }

        // Change status if different
        if (run.getStatus() != status) {
            run.setStatus(status);
        }

        return saveIfStatusChanged
                ? runRepository.save(run)
                : run;
    }

    @Transactional
    public Run createRunFromRequest(User user, Step step, RunCreationRequest request) {
        final ProjectMember member = projectService.getUserMembership(step.getProject(), user)
                .orElseThrow(() -> new BadRequestException("Your have to be a member of this project to create runs"));

        final List<Task> tasks = step.getTasks()
                .stream()
                .filter(task -> request.getTaskIds().contains(task.getId()))
                // Check if there is not already a run for this tasks and for this user
                .filter(task -> task.getRuns()
                        .stream()
                        .noneMatch(runTask -> runTask.getRun()
                                .getMember()
                                .getId()
                                .equals(member.getId())))
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
                .member(member)
                .step(step)
                .status(RunStatus.ACTIVE)
                .limitDate(limitDate)
                .build();

        final List<RunTask> runTasks = tasks.stream()
                .map(task -> RunTask.builder()
                        .task(task)
                        .run(run)
                        .abandoned(false)
                        .build())
                .collect(Collectors.toList());

        run.setTasks(runTasks);

        return runRepository.save(run);
    }
}
