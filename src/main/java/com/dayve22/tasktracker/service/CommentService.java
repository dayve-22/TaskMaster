package com.dayve22.tasktracker.service;

import com.dayve22.tasktracker.dto.CommentRequest;
import com.dayve22.tasktracker.exception.ResourceNotFoundException;
import com.dayve22.tasktracker.model.Comment;
import com.dayve22.tasktracker.model.Task;
import com.dayve22.tasktracker.model.User;
import com.dayve22.tasktracker.repository.CommentRepository;
import com.dayve22.tasktracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private TaskService taskService; // Reuse TaskService for security checks

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public Comment addComment(Long taskId, CommentRequest request, String username) {
        // 1. Fetch Task (This method in TaskService already verifies if User is part of the Project)
        Task task = taskService.getTaskById(taskId, username);

        // 2. Fetch User
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));

        // 3. Create Comment
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setTask(task);
        comment.setUser(user);

        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByTaskId(Long taskId, String username) {
        // Verify access rights via TaskService
        taskService.getTaskById(taskId, username);

        return commentRepository.findByTaskId(taskId);
    }
}
