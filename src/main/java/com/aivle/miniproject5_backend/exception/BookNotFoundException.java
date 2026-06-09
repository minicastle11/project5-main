package com.aivle.miniproject5_backend.exception;

public class BookNotFoundException extends RuntimeException {

    public BookNotFoundException(Long id) {
        super("Book not Found: id = " + id);
    }
}
