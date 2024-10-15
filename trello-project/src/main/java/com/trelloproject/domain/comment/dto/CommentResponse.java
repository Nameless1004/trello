package com.trelloproject.domain.comment.dto;

import com.trelloproject.domain.comment.entity.Comment;
import lombok.Getter;

@Getter
public class CommentResponse {
    private Long commentId;
    private String memberName;
    private String description;

    public CommentResponse(Comment comment) {
        this.commentId = comment.getId();
        this.memberName = comment.getMember().getUser().getUsername();
        this.description = comment.getDescription();
    }
}
