package com.trelloproject.domain.search.dto;

import com.trelloproject.common.enums.CardStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

public sealed interface CardSearchDto permits CardSearchDto.CardSearch, CardSearchDto.CardSearchResult {
    record CardSearch (String title, String description, LocalDate deadline, String manager) implements CardSearchDto {}
    record CardSearchResult (String title, String description, LocalDate deadline, CardStatus status, Long viewCount) implements CardSearchDto {}
}
