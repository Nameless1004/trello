package com.trelloproject.domain.board.entity;

import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.list.entity.CardList;
import com.trelloproject.domain.workspace.entity.Workspace;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Board {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String bgColor;
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardList> lists = new ArrayList<>();

//    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//    Attachment attachment;

    public void imageUrl (String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Board(String title, String bgColor, String imageUrl) {
        this.title = title;
        this.bgColor = bgColor;
        this.imageUrl = imageUrl;
    }

    public void update(String title, String bgColor) {
        this.title = title;
        this.bgColor = bgColor;
    }
}
