package com.trelloproject.domain.board.entity;

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
    Long id;
    String title;
    String bgColor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    List<CardList> lists = new ArrayList<>();

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    Attachment attachment;

    public Board(String title, String bgColor) {
        this.title = title;
        this.bgColor = bgColor;
    }
}
