package com.dodo.gobz.repository;

import com.dodo.gobz.model.Task;
import org.springframework.data.repository.CrudRepository;

public interface TaskRepository extends CrudRepository<Task, Long> {

}
