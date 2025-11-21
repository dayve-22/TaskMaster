package com.dayve22.tasktracker.service;

import com.google.genai.Client;
import com.google.genai.types.GenerateContentResponse;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AIService {

    @Value("${gemini.api.key}")
    private String apiKey;

    private Client client;

    // Initialize the client once when the application starts
    @PostConstruct
    public void init() {
        // Use the builder to securely inject the key from application.properties
        // instead of relying on system-wide environment variables.
        this.client = Client.builder()
                .apiKey(apiKey)
                .build();
    }

    public String generateTaskDescription(String taskTitle) {
        if (apiKey == null || apiKey.isEmpty()) {
            return "Gemini API Key is missing.";
        }

        try {
            // The SDK simplifies the call significantly
            GenerateContentResponse response = client.models.generateContent(
                    "gemini-2.5-pro", // Or "gemini-1.5-flash" depending on availability
                    "Write a professional, concise, 2-sentence task description for a software task titled: " + taskTitle,
                    null
            );

            // Return the text directly from the response
            return response.text();

        } catch (Exception e) {
            return "Failed to generate description: " + e.getMessage();
        }
    }
}
