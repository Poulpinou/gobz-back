package com.dodo.gobz.models;

import com.dodo.gobz.models.common.RunDuration;
import com.dodo.gobz.models.common.RunState;
import com.sun.istack.NotNull;
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
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "runs")
public class Run {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Project project;

    @Column(nullable = false)
    private String name;

    @Column
    private String description;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RunState state;

    @Column
    private Date startDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RunDuration duration;
}
