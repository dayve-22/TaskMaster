package com.dayve22.tasktracker.service;

import com.dayve22.tasktracker.dto.ProjectRequest;
import com.dayve22.tasktracker.exception.ResourceNotFoundException;
import com.dayve22.tasktracker.model.Project;
import com.dayve22.tasktracker.model.User;
import com.dayve22.tasktracker.repository.ProjectRepository;
import com.dayve22.tasktracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class ProjectService {

    @Autowired
    private ProjectRepository projectRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Project createProject(ProjectRequest request, String username) {
        User owner = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        Project project = new Project();
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project.setOwner(owner);
        project.getMembers().add(owner); // Owner is automatically a member

        return projectRepository.save(project);
    }

    public List<Project> getProjectsForUser(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
        // Assuming you added findByMembersContaining in Repository, or we can filter manually
        // Better approach: Add this method to ProjectRepository: List<Project> findByMembers_Id(Long userId);
        return projectRepository.findAll().stream()
                .filter(project -> project.getMembers().contains(user))
                .toList();
    }

    public Project getProjectById(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        // Security check: is the user a member?
        boolean isMember = project.getMembers().stream()
                .anyMatch(m -> m.getUsername().equals(username));

        if (!isMember) {
            throw new RuntimeException("Not authorized to view this project");
        }

        return project;
    }

    @Transactional
    public void deleteProject(Long projectId, String username) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResourceNotFoundException("Project", "id", projectId));

        if (!project.getOwner().getUsername().equals(username)) {
            throw new RuntimeException("Only the owner can delete the project");
        }

        projectRepository.delete(project);
    }

    @Transactional
    public Project addMember(Long projectId, String usernameToAdd, String requesterUsername) {
        Project project = getProjectById(projectId, requesterUsername); // Validates access

        User userToAdd = userRepository.findByUsername(usernameToAdd)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", usernameToAdd));

        project.getMembers().add(userToAdd);
        return projectRepository.save(project);
    }
}
