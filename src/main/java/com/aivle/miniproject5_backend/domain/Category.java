package com.aivle.miniproject5_backend.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum Category {
    FICTION("소설"),
    MYSTERY("추리/스릴러"),
    FANTASY("판타지/SF"),
    ROMANCE("로맨스"),
    HISTORY("역사"),
    SCIENCE("과학"),
    TECHNOLOGY("기술/IT"),
    SELF_HELP("자기계발"),
    ECONOMY("경제/경영"),
    ETC("기타");

    private final String description;


}
