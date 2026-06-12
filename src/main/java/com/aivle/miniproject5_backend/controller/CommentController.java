package com.aivle.miniproject5_backend.controller;

import com.aivle.miniproject5_backend.domain.Comment;
import com.aivle.miniproject5_backend.dto.CommentRequest;
import com.aivle.miniproject5_backend.dto.CommentResponse;
import com.aivle.miniproject5_backend.service.CommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:5173")
public class CommentController {
    private final CommentService commentService;

    @GetMapping("/api/v1/books/{bookId}/comments")
    public List<CommentResponse> getComments(@PathVariable Long bookId) {
        return commentService.findByBookId(bookId).stream()
                .map(CommentResponse::from)
                .toList();
    }

    @PostMapping("/api/v1/books/{bookId}/comments")
    public ResponseEntity<CommentResponse> createComment(@PathVariable Long bookId,
                                                         @Valid @RequestBody CommentRequest request) {
        Comment saved = commentService.create(bookId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(CommentResponse.from(saved));
    }

    @PatchMapping("/api/v1/comments/{commentId}")
    public CommentResponse updateComment(@PathVariable Long commentId,
                                         @Valid @RequestBody CommentRequest request) {
        return CommentResponse.from(commentService.update(commentId, request));
    }

    @DeleteMapping("/api/v1/comments/{commentId}")
    public ResponseEntity<Void> deleteComment(@PathVariable Long commentId) {
        commentService.delete(commentId);
        return ResponseEntity.noContent().build();
    }
}
