package com.trelloproject.domain.search.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.search.dto.CardSearchDto;
import com.trelloproject.domain.search.service.CardSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class CardSearchController {
    private final CardSearchService cardSearchService;

    @GetMapping("/search")
    public ResponseEntity<ResponseDto<Page<CardSearchDto.CardSearchResult>>> searchCards(CardSearchDto.CardSearch searchDto, long boardId, Pageable pageable) {
        ResponseDto<Page<CardSearchDto.CardSearchResult>> response = cardSearchService.searchCards(searchDto, boardId, pageable);
        return ResponseEntity.ok(response);
    }
}
