package com.aivle.miniproject5_backend.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "book")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class) // 감시자로 사용된다. 저장, 수정시간 때문에 사용
public class Book {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    @NotBlank
    private String title;

    @Column(nullable = false)
    @NotBlank
    private String author;

    @Lob
    @Column(nullable = false, columnDefinition = "MEDIUMTEXT") // 255자 제한 및 용량 한계 있어서, 필요(내용 길어짐 대비)
    @NotBlank
    private String content;

    private Integer likes = 0;
    private Integer views = 0;

    @Lob
    @Column(columnDefinition="MEDIUMTEXT") // 255자 제한 및 용량 한계 있어서, 필요(Base64 담기 가능)
    private String coverImageUrl;

    // 생성되면 날짜 자동으로 생김
    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    // 업데이트되면 날짜 자동으로 변경
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Category category;
}
