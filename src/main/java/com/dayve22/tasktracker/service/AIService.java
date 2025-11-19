package com.dayve22.tasktracker.service;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.net.http.HttpHeaders;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIService {

    @Value("${openai.api.key}")
    private String apiKey;

    private final String OPENAI_URL = "https://api.openai.com/v1/chat/completions";
    private final RestTemplate restTemplate = new RestTemplate();

    public String generateTaskDescription(String taskTitle) {
        // If no API key is configured, return a default string to prevent crashing
        if (apiKey == null || apiKey.isEmpty() || apiKey.equals("YOUR_OPENAI_KEY")) {
            return "AI Description generation unavailable (API Key missing).";
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Bearer " + apiKey);

        // Request Body for OpenAI Model
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "gpt-3.5-turbo");

        Map<String, String> message = new HashMap<>();
        message.put("role", "user");
        message.put("content", "Write a professional, concise task description for a software development task titled: " + taskTitle);

        requestBody.put("messages", List.of(message));

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.postForEntity(OPENAI_URL, entity, Map.class);
            // Parse the deep nested JSON response from OpenAI
            // This is a simplified parsing for brevity
            Map<String, Object> body = response.getBody();
            List<Map<String, Object>> choices = (List<Map<String, Object>>) body.get("choices");
            Map<String, Object> firstChoice = choices.get(0);
            Map<String, String> msg = (Map<String, String>) firstChoice.get("message");

            return msg.get("content");
        } catch (Exception e) {
            return "Failed to generate description: " + e.getMessage();
        }
    }
}
