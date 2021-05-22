package com.dodo.gobz.models;

import com.dodo.gobz.models.audits.Auditable;
import com.dodo.gobz.models.common.RunStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "runs")
public class Run extends Auditable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    private Step step;

    @ManyToMany
    @JoinTable(
            name = "run_tasks",
            joinColumns = @JoinColumn(name = "run_id"),
            inverseJoinColumns = @JoinColumn(name = "task_id")
    )
    private List<Task> tasks;

    @Column
    @Enumerated(EnumType.STRING)
    private RunStatus status;

    @Column
    private LocalDate limitDate;

    public boolean hasLimitDate() {
        return limitDate != null;
    }


    public double getCompletion() {
        final List<Task> tasks = this.tasks;
        if (tasks.isEmpty()) {
            return 1;
        }

        final double doneCount = tasks.stream()
                .mapToInt(task -> task.isDone() ? 1 : 0)
                .sum();

        return doneCount / tasks.size();
    }
}
