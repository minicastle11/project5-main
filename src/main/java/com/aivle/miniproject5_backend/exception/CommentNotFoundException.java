package com.aivle.miniproject5_backend.exception;

public class CommentNotFoundException extends RuntimeException {

    public CommentNotFoundException(Long id) {
        super("Comment not Found: id = " + id);
    }
}
