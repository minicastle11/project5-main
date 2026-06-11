package com.aivle.miniproject5_backend.repository;


import com.aivle.miniproject5_backend.domain.Book;
import com.aivle.miniproject5_backend.domain.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Long> {
    List<Book> findByTitleContaining(String keyword);
    List<Book> findByAuthorContaining(String keyword);
    List<Book> findAllByOrderByViewsDesc();
    List<Book> findAllByOrderByLikesDesc();
    List<Book> findByCategory(Category category);
}
