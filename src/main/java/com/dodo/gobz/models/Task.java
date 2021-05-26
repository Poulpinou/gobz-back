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
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "tasks")
public class Task extends Auditable implements ProjectElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Step step;

    @Column(nullable = false)
    private String text;

    @Column
    private boolean done;

    @OneToMany(mappedBy = "task", cascade = CascadeType.ALL)
    private List<RunTask> runs = new ArrayList<>(0);

    @Override
    public Project getProject() {
        return getStep().getChapter().getProject();
    }
}
