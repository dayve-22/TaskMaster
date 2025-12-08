package com.dayve22.tasktracker.controller;

import com.dayve22.tasktracker.dto.TaskRequest;
import com.dayve22.tasktracker.dto.UpdateTaskStatusRequest;
import com.dayve22.tasktracker.model.Task;
import com.dayve22.tasktracker.service.TaskService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TaskController {

    @Autowired
    private TaskService taskService;

    @Autowired
    private com.dayve22.tasktracker.service.AIService aiService;

    // Create Task inside a project
    @PostMapping("/projects/{projectId}/tasks")
    public ResponseEntity<Task> createTask(@PathVariable Long projectId,
                                           @Valid @RequestBody TaskRequest request,
                                           Principal principal) {
        return ResponseEntity.ok(taskService.createTask(projectId, request, principal.getName()));
    }

    @GetMapping("/projects/{projectId}/tasks")
    public ResponseEntity<List<Task>> getProjectTasks(
            @PathVariable Long projectId,
            @RequestParam(required = false) com.dayve22.tasktracker.model.TaskStatus status,
            @RequestParam(required = false) String search,
            Principal principal) {
        return ResponseEntity.ok(taskService.getTasks(projectId, status, search, principal.getName()));
    }

    // Get specific task details
    @GetMapping("/tasks/{taskId:[0-9]+}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long taskId, Principal principal) {
        return ResponseEntity.ok(taskService.getTaskById(taskId, principal.getName()));
    }

    // Update Task Status
    @PutMapping("/tasks/{taskId}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long taskId,
                                                 @Valid @RequestBody UpdateTaskStatusRequest request,
                                                 Principal principal) {
        return ResponseEntity.ok(taskService.updateTaskStatus(taskId, request.getStatus(), principal.getName()));
    }

    // Assign Task to User
    @PutMapping("/tasks/{taskId}/assign/{userId}")
    public ResponseEntity<Task> assignTask(@PathVariable Long taskId,
                                           @PathVariable Long userId,
                                           Principal principal) {
        return ResponseEntity.ok(taskService.assignTask(taskId, userId, principal.getName()));
    }

    @GetMapping("/tasks/generate-ai-description")
    public ResponseEntity<String> generateDescription(@RequestParam String title) {
        return ResponseEntity.ok(aiService.generateTaskDescription(title));
    }

    @GetMapping("/tasks/assigned")
    public ResponseEntity<List<Task>> getAssignedTasks(Principal principal) {
        return ResponseEntity.ok(taskService.getTasksAssignedToUser(principal.getName()));
    }

    // Add Attachment
    @PostMapping("/tasks/{taskId}/attachments")
    public ResponseEntity<Task> addAttachment(@PathVariable Long taskId,
                                              @RequestBody Map<String, String> payload,
                                              Principal principal) {
        return ResponseEntity.ok(taskService.addAttachment(taskId, payload.get("url"), principal.getName()));
    }
}