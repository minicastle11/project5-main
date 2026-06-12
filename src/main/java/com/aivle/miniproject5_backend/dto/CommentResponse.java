package com.aivle.miniproject5_backend.dto;

import com.aivle.miniproject5_backend.domain.Comment;

import java.time.LocalDateTime;

public record CommentResponse(
        Long id,
        Long bookId,
        String writer,
        String content,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static CommentResponse from(Comment comment) {
        return new CommentResponse(
                comment.getId(),
                comment.getBook().getId(),
                comment.getWriter(),
                comment.getContent(),
                comment.getCreatedAt(),
                comment.getUpdatedAt()
        );
    }
}
