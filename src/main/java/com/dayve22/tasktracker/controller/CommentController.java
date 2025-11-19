package com.dayve22.tasktracker.controller;

import com.dayve22.tasktracker.dto.CommentRequest;
import com.dayve22.tasktracker.model.Comment;
import com.dayve22.tasktracker.service.CommentService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/tasks")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/{taskId}/comments")
    public ResponseEntity<Comment> addComment(@PathVariable Long taskId,
                                              @Valid @RequestBody CommentRequest request,
                                              Principal principal) {
        return ResponseEntity.ok(commentService.addComment(taskId, request, principal.getName()));
    }

    @GetMapping("/{taskId}/comments")
    public ResponseEntity<List<Comment>> getComments(@PathVariable Long taskId, Principal principal) {
        return ResponseEntity.ok(commentService.getCommentsByTaskId(taskId, principal.getName()));
    }
}
