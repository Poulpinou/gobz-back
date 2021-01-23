package com.dodo.gobz.model.keys;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
@Data
public class RunTaskKey implements Serializable {
    @Column(name = "run_id")
    private Long runId;

    @Column(name = "task_id")
    private Long taskId;
}
