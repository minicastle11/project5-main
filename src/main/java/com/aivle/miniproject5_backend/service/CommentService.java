package com.aivle.miniproject5_backend.service;

import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.domain.Comment;
import com.aivle.miniproject5_backend.dto.CommentRequest;
import com.aivle.miniproject5_backend.exception.CommentNotFoundException;
import com.aivle.miniproject5_backend.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final BookService bookService;

    @Transactional(readOnly = true)
    public List<Comment> findByBookId(Long bookId) {
        bookService.findById(bookId);
        return commentRepository.findByBookIdOrderByCreatedAtDesc(bookId);
    }

    @Transactional
    public Comment create(Long bookId, CommentRequest request) {
        Book book = bookService.findById(bookId);

        Comment comment = new Comment();
        comment.setBook(book);
        comment.setWriter(request.writer());
        comment.setContent(request.content());

        return commentRepository.save(comment);
    }

    @Transactional
    public Comment update(Long id, CommentRequest request) {
        Comment comment = findById(id);
        comment.setWriter(request.writer());
        comment.setContent(request.content());
        return commentRepository.save(comment);
    }

    @Transactional
    public void delete(Long id) {
        if (!commentRepository.existsById(id)) {
            throw new CommentNotFoundException(id);
        }
        commentRepository.deleteById(id);
    }

    private Comment findById(Long id) {
        return commentRepository.findById(id)
                .orElseThrow(() -> new CommentNotFoundException(id));
    }
}
