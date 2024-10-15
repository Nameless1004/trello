package com.trelloproject.domain.search.repository;

import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.search.dto.CardSearchDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CardSearchRepositoryCustom {
    Page<CardSearchDto.CardSearchResult> searchCards(CardSearchDto.CardSearch searchDto, Long boardId, Pageable pageable);
}
