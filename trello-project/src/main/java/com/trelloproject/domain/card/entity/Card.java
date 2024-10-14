package com.trelloproject.domain.card.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "cards")
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "list_id", nullable = false)
//    private Lists list;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "member_id", nullable = false)
//    private Member member;
//
//    private String title;
//    private String description;
//    private LocalDate deadline;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "manager_id", nullable = false)
//    private Manager manager;
//
//    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<Comment> comments;
//
//    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
//    private List<Attachment> attachments;

    private LocalDate createdAt;
    private LocalDate updatedAt;

    // 조회수 관리 필드 추가
    private Long viewCount = 0L;
}
