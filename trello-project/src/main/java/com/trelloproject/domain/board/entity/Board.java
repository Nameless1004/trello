package com.trelloproject.domain.board.entity;

import com.trelloproject.domain.attachment.dto.S3UploadResponse;
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
    private String s3Key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Workspace workspace;

    @OneToMany(mappedBy = "board", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<CardList> lists = new ArrayList<>();


    public void setImageUrlAndKey (S3UploadResponse s3UploadResponse) {
        if(s3UploadResponse != null) {
            this.imageUrl = s3UploadResponse.getS3Url();
            this.s3Key = s3UploadResponse.getS3Key();
        }
    }

    public Board(Workspace workspace, String title, String bgColor) {
        this.workspace = workspace;
        this.title = title;
        this.bgColor = bgColor;
    }

    public void update(String title, String bgColor) {
        this.title = title;
        this.bgColor = bgColor;
    }
}
