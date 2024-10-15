package com.trelloproject.domain.card.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.card.dto.CardResponse;
import com.trelloproject.domain.card.service.CardService;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/lists/{listId}/cards")
@RequiredArgsConstructor
public class CardController {
    private final CardService cardService;

    // 카드 생성
    @PostMapping
    public ResponseEntity<ResponseDto<CardResponse>> createCard(@AuthenticationPrincipal AuthUser authUser,
                                                                @PathVariable Long listId,
                                                                @RequestBody CardRequest request) {
        return cardService.createOrUpdateCard(authUser, listId, request).toEntity();
    }

    // 카드 조회
    @GetMapping("/{cardId}")
    public ResponseEntity<ResponseDto<CardResponse>> getCardDetails(@PathVariable Long listId,
                                                                    @PathVariable Long cardId) {
        return cardService.getCardDetails(listId, cardId).toEntity();
    }

    // 카드 수정
    @PatchMapping("/{cardId}")
    public ResponseEntity<ResponseDto<CardResponse>> updateCard(@AuthenticationPrincipal AuthUser authUser,
                                                                @PathVariable Long listId,
                                                                @PathVariable Long cardId,
                                                                @RequestBody CardRequest request) {
        return cardService.updateCard(authUser, listId, cardId, request).toEntity();
    }

    // 카드 삭제
    @DeleteMapping("/{cardId}")
    public ResponseEntity<ResponseDto<Void>> deleteCard(@AuthenticationPrincipal AuthUser authUser,
                                                        @PathVariable Long listId,
                                                        @PathVariable Long cardId) {
        return cardService.deleteCard(authUser, listId, cardId).toEntity();
    }
}
