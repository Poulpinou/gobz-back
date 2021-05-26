package com.dodo.gobz.models;

import com.dodo.gobz.models.audits.DateAuditable;
import com.dodo.gobz.models.enums.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "runs")
public class Run extends DateAuditable implements ProjectElement, CompletableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private ProjectMember member;

    @ManyToOne
    private Step step;

    @OneToMany(mappedBy = "run", cascade = {CascadeType.PERSIST})
    private List<RunTask> tasks;

    @Column
    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @Column
    private LocalDate limitDate;

    public boolean hasLimitDate() {
        return limitDate != null;
    }

    public float getCompletion() {
        final List<RunTask> runTasks = this.tasks;
        if (tasks.isEmpty()) {
            return 1;
        }

        final float doneCount = runTasks.stream()
                .map(RunTask::getTask)
                .mapToInt(task -> task.isDone() ? 1 : 0)
                .sum();

        return doneCount / tasks.size();
    }

    @Override
    public Project getProject() {
        return step.getProject();
    }

    public boolean isActive(){
        return status == RunStatus.ACTIVE || status == RunStatus.LATE;
    }
}
