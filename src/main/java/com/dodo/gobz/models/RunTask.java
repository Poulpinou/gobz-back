package com.dodo.gobz.models;

import com.dodo.gobz.models.common.RunTaskType;
import com.dodo.gobz.models.keys.RunTaskKey;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.MapsId;
import javax.persistence.Table;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "run_tasks")
public class RunTask {
    @EmbeddedId
    private RunTaskKey id;

    @ManyToOne(optional = false)
    @MapsId("runId")
    @JoinColumn(name = "run_id")
    private Run run;

    @ManyToOne(optional = false)
    @MapsId("taskId")
    @JoinColumn(name = "task_id")
    private Task task;

    @NotNull
    @Enumerated(EnumType.STRING)
    private RunTaskType type;
}
