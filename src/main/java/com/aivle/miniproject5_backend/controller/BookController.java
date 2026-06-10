package com.aivle.miniproject5_backend.controller;

import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RequestMapping("/api/v1/books") // Request에 일일히 쓸 필요 없이 매핑
@RestController
@RequiredArgsConstructor // 생성자를 만들어 줌
public class BookController {
    private final BookService bookService;

    // 하나의 책
    @GetMapping("/{id}")
    public Book getBook(@PathVariable Long id) {
        return bookService.findById(id);
    }

    // 모든 책
    @GetMapping
    public List<Book> getAll() {
        return bookService.findAll();
    }
    // 페이징 모든 책
    @GetMapping("/page")
    public Page<Book> getPage(@RequestParam(defaultValue="0") int page,
                              @RequestParam(defaultValue="8") int size,
                              @RequestParam(defaultValue="createdAt") String sortBy) {
        return bookService.findPage(page, size, sortBy);
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
        bookService.deleteBook(id);
        return ResponseEntity.noContent().build();
    }

    // 등록
    @PostMapping
    public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
        Book saved = bookService.create(book);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // 수정
    @PatchMapping("/{id}")
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        return bookService.update(id, book);
    }

    // 표지 이미지 URL 업데이트
    @PatchMapping("/{id}/cover-update")
    public Book updateBookWithCover(@PathVariable Long id, @RequestBody Book book) {
        return bookService.updateBookWithCover(id, book);
    }

    // 좋아요 수 증가
    @PatchMapping("/{id}/likes")
    public ResponseEntity<Book> addLikes(@PathVariable Long id) {
        Book updatedBook = bookService.addLikes(id);
        return ResponseEntity.ok(updatedBook);
    }

    // 조회 수 증가
    @PatchMapping("/{id}/views")
    public ResponseEntity<Book> addViews(@PathVariable Long id) {
        Book updatedBook = bookService.addViews(id);
        return ResponseEntity.ok(updatedBook);
    }

    // 책 검색
    @GetMapping("/search")
    public List<Book> searchBooks(@RequestParam(required = false) String title,
                                  @RequestParam(required = false) String author) {
        if (title != null) {
            return bookService.searchByTitleContaining(title);
        } else if (author != null) {
            return bookService.searchByAuthorContaining(author);
        } else {
            return bookService.findAll();
        }
    }
}
