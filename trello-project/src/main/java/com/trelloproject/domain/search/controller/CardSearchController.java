package com.trelloproject.domain.search.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.search.dto.CardSearchDto;
import com.trelloproject.domain.search.service.CardSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CardSearchController {
    private final CardSearchService cardSearchService;

    @GetMapping("/boards/{boardId}/search")
    public ResponseEntity<ResponseDto<Page<CardSearchDto.CardSearchResult>>> searchCards(@RequestBody CardSearchDto.CardSearch searchDto, @PathVariable long boardId, @PageableDefault(size = 10) Pageable pageable) {
        ResponseDto<Page<CardSearchDto.CardSearchResult>> response = cardSearchService.searchCards(searchDto, boardId, pageable);
        return ResponseEntity.ok(response);
    }
}
