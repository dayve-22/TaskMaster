package com.dayve22.tasktracker.repository;

import com.dayve22.tasktracker.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Long> {
    // We will add methods like findByOwner(User user)
}
