package com.aivle.miniproject5_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    FICTION("소설"),
    TECHNOLOGY("기술/IT"),
    SELF_HELP("자기계발"),
    ETC("기타");

    private final String description;


}
