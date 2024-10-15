package com.trelloproject.domain.card.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.card.dto.CardResponse;
import com.trelloproject.domain.card.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/boards/{boardId}/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<ResponseDto<CardResponse>> createCard(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @RequestBody CardRequest request) {
        // 서비스 계층에 보드 ID와 리스트 ID를 전달
        return cardService.createOrUpdateCard(boardId, listId, request).toEntity();
    }

    // 카드 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<ResponseDto<CardResponse>> getCardDetails(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long cardId) {
        // 서비스 계층에 보드 ID와 리스트 ID를 전달
        return cardService.getCardDetails(boardId, listId, cardId).toEntity();
    }

    // 카드 수정
    @PatchMapping("/{cardId}")
    public ResponseEntity<ResponseDto<CardResponse>> updateCard(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long cardId,
            @RequestBody CardRequest request) {
        // 서비스 계층에 보드 ID와 리스트 ID를 전달
        return cardService.updateCard(boardId, listId, cardId, request).toEntity();
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResponseDto<Void>> deleteCard(
            @PathVariable Long boardId,
            @PathVariable Long listId,
            @PathVariable Long cardId) {
        // 서비스 계층에 보드 ID와 리스트 ID를 전달
        return cardService.deleteCard(boardId, listId, cardId).toEntity();
    }
}
