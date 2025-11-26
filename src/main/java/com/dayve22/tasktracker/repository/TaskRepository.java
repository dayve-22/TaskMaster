package com.dayve22.tasktracker.repository;

import com.dayve22.tasktracker.model.Task;
import com.dayve22.tasktracker.model.TaskStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TaskRepository extends JpaRepository<Task, Long> {

    List<Task> findByProjectId(Long projectId);

    // For "My Tasks"
    List<Task> findByAssigneeId(Long assigneeId);

    // For Filtering by Status
    List<Task> findByProjectIdAndStatus(Long projectId, TaskStatus status);

    // For Searching by Title or Description (Case insensitive)
    @Query("SELECT t FROM Task t WHERE t.project.id = :projectId AND " +
            "(LOWER(t.title) LIKE LOWER(CONCAT('%', :keyword, '%')) OR " +
            "LOWER(t.description) LIKE LOWER(CONCAT('%', :keyword, '%')))")
    List<Task> searchTasks(@Param("projectId") Long projectId, @Param("keyword") String keyword);
}
