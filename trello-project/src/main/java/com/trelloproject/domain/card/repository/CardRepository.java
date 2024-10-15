package com.trelloproject.domain.card.repository;

import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.search.repository.CardSearchRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long>, CardSearchRepositoryCustom {
    @Query("SELECT c FROM Card c WHERE c.cardList.board.id = :boardId")
    List<Card> findByBoardId(@Param("boardId") Long boardId);
}
