package com.dayve22.tasktracker.dto;

import lombok.Data;

// A generic response for success/error messages
@Data
public class ApiResponse {
    private boolean success;
    private String message;

    public ApiResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
