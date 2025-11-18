package com.dayve22.tasktracker.dto;

import com.dayve22.tasktracker.model.TaskStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateTaskStatusRequest {
    @NotNull
    private TaskStatus status;
}
