package com.trelloproject.domain.manager.entity;

import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.member.entity.Member;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Manager {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;  // Member와의 ManyToOne 관계 설정

    @ManyToMany(mappedBy = "managers")  // Card 엔티티에서 설정된 관계와 매핑
    private List<Card> cards = new ArrayList<>();

    private String name;
    private String role;
}