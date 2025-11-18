package com.dayve22.tasktracker.service;

import com.dayve22.tasktracker.dto.TaskRequest;
import com.dayve22.tasktracker.exception.ResourceNotFoundException;
import com.dayve22.tasktracker.model.Project;
import com.dayve22.tasktracker.model.Task;
import com.dayve22.tasktracker.model.TaskStatus;
import com.dayve22.tasktracker.model.User;
import com.dayve22.tasktracker.repository.ProjectRepository;
import com.dayve22.tasktracker.repository.TaskRepository;
import com.dayve22.tasktracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectService projectService; // Use ProjectService to reuse access logic

    @Transactional
    public Task createTask(Long projectId, TaskRequest request, String username) {
        // Validate project existence and user access using ProjectService
        Project project = projectService.getProjectById(projectId, username);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setDueDate(request.getDueDate());
        task.setStatus(TaskStatus.OPEN);
        task.setProject(project);

        // Handle Assignee
        if (request.getAssigneeId() != null) {
            User assignee = userRepository.findById(request.getAssigneeId())
                    .orElseThrow(() -> new ResourceNotFoundException("User", "id", request.getAssigneeId()));
            // Optional: Check if assignee is actually a member of the project
            if (!project.getMembers().contains(assignee)) {
                throw new RuntimeException("Assignee must be a member of the project");
            }
            task.setAssignee(assignee);
        }

        return taskRepository.save(task);
    }

    public List<Task> getTasksForProject(Long projectId, String username) {
        // Validate access
        projectService.getProjectById(projectId, username);
        return taskRepository.findByProjectId(projectId);
    }

    public Task getTaskById(Long taskId, String username) {
        Task task = taskRepository.findById(taskId)
                .orElseThrow(() -> new ResourceNotFoundException("Task", "id", taskId));

        // Validate access via project
        projectService.getProjectById(task.getProject().getId(), username);

        return task;
    }

    @Transactional
    public Task updateTaskStatus(Long taskId, TaskStatus status, String username) {
        Task task = getTaskById(taskId, username); // This handles access validation
        task.setStatus(status);
        return taskRepository.save(task);
    }

    @Transactional
    public Task assignTask(Long taskId, Long userId, String username) {
        Task task = getTaskById(taskId, username);
        Project project = task.getProject();

        User assignee = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User", "id", userId));

        if (!project.getMembers().contains(assignee)) {
            throw new RuntimeException("User must be a member of the project to be assigned tasks");
        }

        task.setAssignee(assignee);
        return taskRepository.save(task);
    }
}
