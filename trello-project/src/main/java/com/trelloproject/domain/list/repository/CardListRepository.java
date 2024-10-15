package com.trelloproject.domain.list.repository;

import com.trelloproject.domain.board.entity.Board;
import com.trelloproject.domain.list.entity.CardList;
import jakarta.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CardListRepository extends JpaRepository<CardList, Long> {

    @Query("SELECT cl FROM CardList cl WHERE cl.board=:board ORDER BY cl.orderIndex")
    List<CardList> findAllByBoardOrderByOrder(@Param("board") Board board);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT cl FROM CardList cl WHERE cl.board=:board ORDER BY cl.orderIndex")
    List<CardList> findAllByBoardOrderByOrderWithWriteLock(@Param("board") Board board);

    @Query("SELECT max(cl.orderIndex) FROM CardList cl")
    int maxOrderIndex();

    @Modifying
    @Query("UPDATE CardList cl SET cl.orderIndex = cl.orderIndex + :dir WHERE cl.id IN :cardListIds")
    void updateOrderInIds(@Param("cardListIds") List<Long> cardListIds, @Param("dir") int dir);
}
