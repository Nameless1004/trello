package com.trelloproject.domain.search.service;

import com.trelloproject.common.annotations.ExecutionTimeLog;
import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.search.dto.CardSearchDto;
import com.trelloproject.domain.search.repository.CardSearchRepositoryCustom;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;

@Service
@RequiredArgsConstructor
public class CardSearchService {
    private final CardRepository cardRepository;

    @ExecutionTimeLog
    @Transactional(readOnly = true)
    public ResponseDto<Page<CardSearchDto.CardSearchResult>> searchCards(CardSearchDto.CardSearch request, long boardId, Pageable pageable) {
        Page<CardSearchDto.CardSearchResult> cards = cardRepository.searchCards(request, boardId, pageable);

        return ResponseDto.of(HttpStatus.OK, cards);
    }
}
