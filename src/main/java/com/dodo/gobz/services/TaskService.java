package com.dodo.gobz.services;

import com.dodo.gobz.models.Task;
import com.dodo.gobz.repositories.TaskRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TaskService {

    final TaskRepository taskRepository;

    @Transactional
    public Task updateTaskStatus(Task task, boolean isDone) {
        task.setDone(isDone);

        return taskRepository.save(task);
    }
}
