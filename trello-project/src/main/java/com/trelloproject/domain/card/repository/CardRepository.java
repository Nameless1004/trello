package com.trelloproject.domain.card.repository;

import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.search.repository.CardSearchRepositoryCustom;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long>, CardSearchRepositoryCustom {
    @Query("SELECT c FROM Card c WHERE c.cardList.board.id = :boardId")
    List<Card> findByBoardId(@Param("boardId") Long boardId);

    @Query("SELECT cl FROM Card cl JOIN FETCH cl.cardList b JOIN FETCH b.board c JOIN FETCH c.workspace WHERE cl.id = :cardId")
    Optional<Card> findWithCardListAndBoardAndWorkspaceIdByCardId(@Param("cardId") long cardId);

    @Override
    @Lock(LockModeType.OPTIMISTIC)
    Optional<Card> findById(Long _id13);
}
