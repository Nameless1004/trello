package com.trelloproject.domain.list.entity;

import com.trelloproject.domain.board.entity.Board;
import com.trelloproject.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

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

//    @OneToMany(mappedBy="~~")
//    private List<Card> cards = new ArrayList<>();

    @Builder
    public CardList(String title, int order) {
        this.title = title;
        this.orderIndex = order;
    }

    // test하고 지우기
    public CardList(Long id, String title, int order) {
        this.id = id;
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
