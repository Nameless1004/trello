package com.trelloproject.domain.attachment.entity;
import com.trelloproject.domain.card.entity.Card;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class Attachment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String s3Url;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Attachment(String s3Url, Card card) {
        this.s3Url = s3Url;
        this.card = card;
    }

    public Attachment(Attachment attachment) {
        this.s3Url = attachment.s3Url;
        this.card = attachment.card;
    }
}
