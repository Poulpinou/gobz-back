package com.dodo.gobz.repositories;

import com.dodo.gobz.models.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
