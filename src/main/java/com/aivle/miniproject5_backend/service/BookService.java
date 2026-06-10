package com.aivle.miniproject5_backend.service;

import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.exception.BookNotFoundException;
import com.aivle.miniproject5_backend.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {
    private final BookRepository bookRepository;

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
            bookRepository.deleteById(id);
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

        return bookRepository.save(book);
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

        return bookRepository.save(existing);
    }

}
