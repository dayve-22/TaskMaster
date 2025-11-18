package com.dayve22.tasktracker.controller;

import com.dayve22.tasktracker.dto.ProjectRequest;
import com.dayve22.tasktracker.model.Project;
import com.dayve22.tasktracker.service.ProjectService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping
    public ResponseEntity<Project> createProject(@Valid @RequestBody ProjectRequest request, Principal principal) {
        Project project = projectService.createProject(request, principal.getName());
        return ResponseEntity.ok(project);
    }

    @GetMapping
    public ResponseEntity<List<Project>> getMyProjects(Principal principal) {
        return ResponseEntity.ok(projectService.getProjectsForUser(principal.getName()));
    }

    @GetMapping("/{projectId}")
    public ResponseEntity<Project> getProjectById(@PathVariable Long projectId, Principal principal) {
        return ResponseEntity.ok(projectService.getProjectById(projectId, principal.getName()));
    }

    @DeleteMapping("/{projectId}")
    public ResponseEntity<?> deleteProject(@PathVariable Long projectId, Principal principal) {
        projectService.deleteProject(projectId, principal.getName());
        return ResponseEntity.ok("Project deleted successfully");
    }

    @PostMapping("/{projectId}/members")
    public ResponseEntity<?> addMember(@PathVariable Long projectId, @RequestParam String username, Principal principal) {
        return ResponseEntity.ok(projectService.addMember(projectId, username, principal.getName()));
    }
}
