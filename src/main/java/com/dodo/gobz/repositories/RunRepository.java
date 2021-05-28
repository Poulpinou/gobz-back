package com.dodo.gobz.repositories;

import com.dodo.gobz.models.Run;
import com.dodo.gobz.models.enums.RunStatus;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RunRepository extends CrudRepository<Run, Long> {
    List<Run> getRunsByMemberUserId(long userId);

    int countByMemberUserIdAndStatusIn(long userId, RunStatus... statuses);
}
