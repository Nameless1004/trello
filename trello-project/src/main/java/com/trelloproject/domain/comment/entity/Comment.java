package com.trelloproject.domain.comment.entity;

import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Entity
@Getter
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private Card card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Member member;  // 댓글 작성자

    private String description;

    public Comment(Card card, Member member, String description) {
        this.card = card;
        this.member = member;
        this.description = description;
    }

    public void updateComment(String description) {
        this.description = description;
    }
}

