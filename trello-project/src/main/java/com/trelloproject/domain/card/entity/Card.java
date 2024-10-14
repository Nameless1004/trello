package com.trelloproject.domain.card.entity;

import com.trelloproject.common.entity.Timestamped;
import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.comment.entity.Comment;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.manager.entity.Manager;
import com.trelloproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cards")
public class Card extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cardlist_id", nullable = false)
    private CardList cardList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    private String title;
    private String description;
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "manager_id", nullable = false)
    private Manager manager;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    // 조회수 관리 필드 추가
    private Long viewCount = 0L;
}
