package com.aivle.miniproject5_backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CommentRequest(
        @NotBlank
        @Size(max = 50)
        String writer,

        @NotBlank
        @Size(max = 1000)
        String content
) {
}
