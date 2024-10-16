package com.trelloproject.domain.card.entity;

import com.trelloproject.common.entity.Timestamped;
import com.trelloproject.common.enums.CardStatus;
import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.card.dto.CardRequest;
import com.trelloproject.domain.comment.entity.Comment;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.manager.entity.Manager;
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

    private String title;
    private String description;
    private LocalDate deadline;

    @Enumerated(EnumType.STRING)
    private CardStatus status;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "card_managers",
            joinColumns = @JoinColumn(name = "card_id"),
            inverseJoinColumns = @JoinColumn(name = "manager_id")
    )
    private List<Manager> managers = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, fetch = FetchType.LAZY)
    private List<Attachment> attachments = new ArrayList<>();

    private Long viewCount = 0L;

    public Card(CardRequest request, List<Manager> managers) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.deadline = request.getDeadline();
        this.status = CardStatus.of(request.getStatus());
        this.managers = managers;
    }

    public void updateCard(CardRequest request, List<Manager> managers) {
        this.title = request.getTitle();
        this.description = request.getDescription();
        this.deadline = request.getDeadline();
        this.status = CardStatus.of(request.getStatus());
        this.managers = managers;
    }

    public void setCardList(CardList cardList) {
        this.cardList = cardList;
        cardList.getCards().add(this);
    }
}
