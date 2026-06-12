package com.aivle.miniproject5_backend.controller;

import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.domain.Category;
import com.aivle.miniproject5_backend.service.BookService;
import com.aivle.miniproject5_backend.service.RequestThrottleService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api/v1/books") // Request에 일일히 쓸 필요 없이 매핑
@RestController
@RequiredArgsConstructor // 생성자를 만들어 줌
@CrossOrigin(origins = "http://localhost:5173")
public class BookController {
    private final BookService bookService;
    private final RequestThrottleService requestThrottleService;

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
                              @RequestParam(defaultValue="createdAt") String sortBy,
                              @RequestParam(required=false) String category) {
        return bookService.findPage(page, size, sortBy, category);
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
    public ResponseEntity<?> addLikes(@PathVariable Long id, HttpServletRequest request) {
        String clientIp = extractClientIp(request);

        if (!requestThrottleService.canIncreaseLike(id, clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "좋아요는 잠시 후 다시 시도해주세요."));
        }

        Book updatedBook = bookService.addLikes(id);
        return ResponseEntity.ok(updatedBook);
    }

    // 조회 수 증가
    @PatchMapping("/{id}/views")
    public ResponseEntity<?> addViews(@PathVariable Long id, HttpServletRequest request) {
        String clientIp = extractClientIp(request);

        if (!requestThrottleService.canIncreaseView(id, clientIp)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("message", "조회수는 잠시 후 다시 반영됩니다."));
        }

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

    // 좋아요나 조회수 순으로 정렬
    @GetMapping("/sort")
    public List<Book> getSortedBooks(@RequestParam String type) {
        if ("views".equals(type)) {
            return bookService.findAllByViews();
        } else if ("likes".equals(type)) {
            return bookService.findAllByLikes();
        }
        return bookService.findAll();
    }

    // 이미지 처리
    // 생성
    @PostMapping("/cover")
    public ResponseEntity<Map<String, String>> makeImg(@RequestBody Book book,
                                                       @RequestParam String apiKey,
                                                       @RequestParam String imageSize
                          ) {
        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Missing API Key", "message", "API Key가 입력되지 않았습니다."));
        }
        String b64 = bookService.generateAndSaveImage(apiKey, book, imageSize);
        return ResponseEntity.ok(Map.of("b64Json", b64));
    }

    @GetMapping("/categories")
    public List<Map<String, String>> getCategories() {
        return Arrays.stream(Category.values()).map(category ->
                Map.of("name", category.name(), "description", category.getDescription()
                )).collect(Collectors.toList());
    }

    @PostMapping("/category-recommend")
    public ResponseEntity<Map<String, String>> recommendCategory(@RequestBody Map<String, String> body,
                                                                 @RequestParam String apiKey) {
        if (apiKey == null || apiKey.isBlank()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("error", "Missing API Key", "message", "API Key를 입력해주세요."));
        }
        Map<String, String> result = bookService.recommendCategory(apiKey, body.get("title"), body.get("content"));
        return ResponseEntity.ok(result);
    }

    @GetMapping("/category")
    public List<Book> getBooksByCategory(@RequestParam String category) {
        return bookService.searchByCategory(Category.valueOf(category));
    }


    private String extractClientIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");

        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        return request.getRemoteAddr();
    }
    // delete
    // 삭제
    // @DeleteMapping("/cover/{id}")

    // book 업로드
    // patch
}
