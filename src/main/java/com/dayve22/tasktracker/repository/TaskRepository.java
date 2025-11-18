package com.dayve22.tasktracker.repository;

import com.dayve22.tasktracker.model.Task;
import com.dayve22.tasktracker.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {
    List<Task> findByProjectId(Long projectId);
    List<Task> findByAssigneeId(Long userId);
    List<Task> findByStatus(TaskStatus status);
}
