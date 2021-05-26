package com.dodo.gobz.models;

import com.dodo.gobz.models.audits.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "steps")
public class Step extends Auditable implements ProjectElement, CompletableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Chapter chapter;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "step", cascade = CascadeType.ALL)
    private List<Task> tasks;

    @OneToMany(mappedBy = "step", cascade = CascadeType.REMOVE)
    private List<Run> runs;

    public float getCompletion() {
        final List<Task> tasks = this.tasks;
        if (tasks.isEmpty()) {
            return 0;
        }

        final float doneCount = tasks.stream()
                .mapToInt(task -> task.isDone() ? 1 : 0)
                .sum();

        return doneCount / tasks.size();
    }

    @Override
    public Project getProject() {
        return getChapter().getProject();
    }
}
