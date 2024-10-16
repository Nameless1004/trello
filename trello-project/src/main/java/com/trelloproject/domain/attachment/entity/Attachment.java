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
    private String s3Key;

    @ManyToOne
    @JoinColumn(name = "card_id")
    private Card card;

    public Attachment(String s3Url, String s3Key, Card card) {
        this.s3Url = s3Url;
        this.s3Key = s3Key;
        this.card = card;
    }

    public void saveImage(String s3Url) {
        this.s3Url = s3Url;
    }
}
