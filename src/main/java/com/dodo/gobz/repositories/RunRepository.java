package com.dodo.gobz.repositories;

import com.dodo.gobz.models.Run;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RunRepository extends CrudRepository<Run, Long> {
    List<Run> getRunsByMemberUserId(long userId);

    int countRunsByActiveIsTrueAndMemberUserId(long userId);
}
