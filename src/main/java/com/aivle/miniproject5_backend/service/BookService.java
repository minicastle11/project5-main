package com.aivle.miniproject5_backend.service;

import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.domain.Category;
import com.aivle.miniproject5_backend.exception.BookNotFoundException;
import com.aivle.miniproject5_backend.repository.BookRepository;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClient;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;
    private final RestClient restClient = RestClient.create();


    // id로 찾기
    @Transactional(readOnly = true)
    public Book findById(Long id) {
        return bookRepository.findById(id).orElseThrow(()-> new BookNotFoundException(id));
    }

    // 모든 데이터
    @Transactional(readOnly = true)
    public List<Book> findAll() {
        Sort sort = Sort.by("createdAt").descending();
        return bookRepository.findAll(sort);
    }
    // 페이징
    @Transactional(readOnly = true)
    public Page<Book> findPage(int page, int size, String sortBy) {
        Sort sort = Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return bookRepository.findAll(pageable);
    }

    // 책 삭제
    @Transactional
    public void deleteBook(Long id) {

        if(bookRepository.existsById(id)) {
            Book oldBook = bookRepository.findById(id).orElseThrow(() -> new BookNotFoundException(id));
            String oldImageUrl = oldBook.getCoverImageUrl();

            bookRepository.deleteById(id);

            if(oldImageUrl != null && oldImageUrl.startsWith("/uploads/")) {
                try {
                    Path oldPath = Paths.get(oldImageUrl.substring(1)); //앞의 "/" 제거
                    Files.deleteIfExists(oldPath);
                } catch (IOException e) {
                    System.out.println("삭제할려는 주소값은 없습니다.");
                }
            }

        } else {
            throw new BookNotFoundException(id);
        }
    }

    // 제목을 키워드로 검색
    @Transactional(readOnly = true)
    public List<Book> searchByTitleContaining(String keyword) {
        return bookRepository.findByTitleContaining(keyword);
    }

    // 저자를 키워드로 검색
    @Transactional(readOnly = true)
    public List<Book> searchByAuthorContaining(String keyword) {
        return bookRepository.findByAuthorContaining(keyword);
    }
    // 책 추가
    @Transactional
    public Book create(Book book) {
        if (book.getLikes() == null) book.setLikes(0);
        if (book.getViews() == null) book.setViews(0);
        // 카테고리 추가
        if (book.getCategory() != null) {
            book.setCategory(book.getCategory());
        }
        if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().equals("/noImage.jpg")) {
            // 생성된 이미지 정보를 받아서, 백엔드 단의 저장소에 저장
            try {
                String coverImageUrl = book.getCoverImageUrl();
                String base64Data = coverImageUrl.contains(",")
                        ? coverImageUrl.split(",")[1]   // "data:image/png;base64," 제거
                        : coverImageUrl;

                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                String filename = UUID.randomUUID() + ".png"; //id 생성 전이라 UUID로 식별화
                Path path = Paths.get("uploads/images/" + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, imageBytes);

                book.setCoverImageUrl("/uploads/images/" + filename);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        return bookRepository.save(book);
    }

    @Transactional(readOnly = true)
    public List<Book> searchByCategory(Category category) {
        return bookRepository.findByCategory(category);
    }

    // 책 업데이트
    @Transactional
    public Book update(Long id, Book book) {
        Book existing = findById(id);

        if (book.getTitle() != null) {
            existing.setTitle(book.getTitle());
        }
        if (book.getAuthor() != null) {
            existing.setAuthor(book.getAuthor());
        }
        if (book.getContent() != null) {
            existing.setContent(book.getContent());
        }
        if (book.getCoverImageUrl() != null) {
            existing.setCoverImageUrl(book.getCoverImageUrl());
        }
        if (book.getCategory() != null) {
            existing.setCategory(book.getCategory());
        }
        return bookRepository.save(existing);
    }

    // 좋아요 수 증가
    @Transactional
    public Book addLikes(Long id) {
        Book book = findById(id);
        book.setLikes(book.getLikes() + 1);
        return bookRepository.save(book);
    }

    // 조회 수 증가
    @Transactional
    public Book addViews(Long id) {
        Book book = findById(id);
        book.setViews(book.getViews() + 1);
        return bookRepository.save(book);
    }
    
    // 표지 이미지 URL 업데이트
    @Transactional
    public Book updateBookWithCover(Long id, Book book) {
        Book existing = findById(id);

        if ( book.getTitle() != null && !book.getTitle().equals(existing.getTitle()) ) {
            existing.setTitle(book.getTitle());
        }
        if ( book.getAuthor() != null && !book.getAuthor().equals(existing.getAuthor()) ) {
            existing.setAuthor(book.getAuthor());
        }
        if ( book.getContent() != null && !book.getContent().equals(existing.getContent()) ) {
            existing.setContent(book.getContent());
        }
        if ( book.getCategory() != null && !book.getCategory().equals(existing.getCategory()) ) {
            existing.setCategory(book.getCategory());
        }
        
        if (book.getCoverImageUrl() != null && !book.getCoverImageUrl().equals("/noImage.jpg") && !book.getCoverImageUrl().equals(existing.getCoverImageUrl())) {
            String oldImageUrl = existing.getCoverImageUrl();
            if(oldImageUrl != null && oldImageUrl.startsWith("/uploads/")) {
                try {
                    Path oldPath = Paths.get(oldImageUrl.substring(1)); //앞의 "/" 제거
                    Files.deleteIfExists(oldPath);
                } catch (IOException e) {
                    System.out.println("삭제할려는 주소값은 없습니다.");
                }
            }
            // 생성된 이미지 정보를 받아서, 백엔드 단의 저장소에 저장
            try {
                String coverImageUrl = book.getCoverImageUrl();
                String base64Data = coverImageUrl.contains(",")
                        ? coverImageUrl.split(",")[1]   // "data:image/png;base64," 제거
                        : coverImageUrl;

                byte[] imageBytes = Base64.getDecoder().decode(base64Data);
                String filename = UUID.randomUUID() + ".png"; //id 생성 전이라 UUID로 식별화
                Path path = Paths.get("uploads/images/" + filename);
                Files.createDirectories(path.getParent());
                Files.write(path, imageBytes);

                existing.setCoverImageUrl("/uploads/images/" + filename);
            } catch (IOException e) {
                throw new RuntimeException("이미지 저장 실패", e);
            }
        }

        return bookRepository.save(existing);
    }

    @Transactional (readOnly = true)
    public List<Book> findAllByViews() {
        return bookRepository.findAllByOrderByViewsDesc();
    }
    @Transactional (readOnly = true)
    public List<Book> findAllByLikes() {
        return bookRepository.findAllByOrderByLikesDesc();
    }


    public String generateAndSaveImage(String apiKey, Book book, String imageSize) {
        OpenAiResponse response = restClient.post()
                .uri("https://api.openai.com/v1/images/generations")
                .header("Authorization", "Bearer " + apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Map.of(
                        "model", "gpt-image-2",
                        "prompt", """
                                # 역할
                                    너는 북커버 제작 담당자야.
                                
                                    # 지침
                                    - 북커버의 앞면 표지만을 보여줄 것
                                    - 전문적인 북커버 디자인, 높은 퀄리티의 일러스트레이션, 두드러진 시각적 표현, 작품에 적합한 안전성
                                    - 이야기의 분위기나 무드를 포함
                                
                                    # 책 정보
                                    - 제목 : """ + book.getTitle() + "\n" +
                                    "- 내용 요약 :" +  book.getContent()
                                ,
                        "n", 1,
                        "size", imageSize
                ))
                .retrieve()
                .body(OpenAiResponse.class);
        // 파일 저장
        return response.data().get(0).b64Json();
    }

    public String saveImage(String b64Json, Long id) {
        try {
            byte[] imageBytes = Base64.getDecoder().decode(b64Json);
            String filename = id + ".png";
            Path path = Paths.get("uploads/images/" + filename);
            Files.createDirectories(path.getParent());
            Files.write(path, imageBytes);
            return "/uploads/images/" + filename;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
        }
    }

    // 응답 파싱
    public record OpenAiResponse(List<ImageData> data) {
        private record ImageData(@JsonProperty("b64_json") String b64Json) {}
    }

}
