package com.dayve22.tasktracker.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import java.time.LocalDate;

@Data
public class TaskRequest {
    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @FutureOrPresent(message = "Due date must be in the future or today")
    private LocalDate dueDate;

    private Long assigneeId; // Optional: Assign immediately upon creation
}
