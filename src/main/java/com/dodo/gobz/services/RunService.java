package com.dodo.gobz.services;

import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.Task;
import com.dodo.gobz.models.common.RunStatus;
import com.dodo.gobz.repositories.RunRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class RunService {
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
        if (run.getTasks().stream().allMatch(Task::isDone)) {
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
}
