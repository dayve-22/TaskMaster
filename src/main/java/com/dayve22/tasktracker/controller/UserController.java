package com.dayve22.tasktracker.controller;

import com.dayve22.tasktracker.exception.ResourceNotFoundException;
import com.dayve22.tasktracker.model.User;
import com.dayve22.tasktracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserRepository userRepository;

    // User Story: View my profile
    @GetMapping("/me")
    public ResponseEntity<User> getCurrentUser(Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", principal.getName()));
        return ResponseEntity.ok(user);
    }

    // User Story: Update my personal information
    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestBody Map<String, String> updates, Principal principal) {
        User user = userRepository.findByUsername(principal.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", principal.getName()));

        if (updates.containsKey("fullName")) {
            user.setFullName(updates.get("fullName"));
        }
        // Note: Email updates usually require re-verification, skipping for simplicity
        if (updates.containsKey("email")) {
            user.setEmail(updates.get("email"));
        }

        return ResponseEntity.ok(userRepository.save(user));
    }
}
