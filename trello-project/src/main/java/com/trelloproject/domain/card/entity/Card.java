package com.trelloproject.domain.card.entity;

import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.comment.entity.Comment;
import com.trelloproject.domain.manager.entity.Manager;
import com.trelloproject.domain.list.entiry.Lists;
import com.trelloproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id", nullable = false)
    private Lists list;

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
    private List<Comment> comments;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Attachment> attachments;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 조회수 관리 필드 추가
    private Long viewCount = 0L;
}
