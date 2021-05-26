package com.dodo.gobz.models;

import com.dodo.gobz.models.audits.Auditable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
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
@Table(name = "chapters")
public class Chapter extends Auditable implements ProjectElement, CompletableElement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @OneToMany(mappedBy = "chapter", cascade = CascadeType.ALL)
    private List<Step> steps;

    public float getCompletion() {
        final List<Step> steps = this.steps;
        if (steps.isEmpty()) {
            return 0;
        }
        return (float) (steps.stream()
                .mapToDouble(Step::getCompletion)
                .sum() / steps.size());
    }
}
