package com.trelloproject.domain.list.entity;

import com.trelloproject.domain.board.entity.Board;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CardList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    
    // 오더는 0부터 시작
    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="board_id")
    private Board board;

    @Builder
    public CardList(String title, int order) {
        this.title = title;
        this.orderIndex = order;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateOrder(int l) {
        this.orderIndex = l;
    }
}
