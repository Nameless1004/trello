package com.trelloproject.domain.card.dto;

import com.trelloproject.common.enums.CardStatus;
import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.manager.dto.ManagerResponse;

import com.trelloproject.domain.comment.dto.CommentResponse;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class CardResponse {
    private Long cardId;
    private String title;
    private String description;
    private LocalDate deadline;
    private CardStatus status;
    private List<ManagerResponse> managers;  // 매니저 정보를 ManagerResponse로 반환

    private List<String> attachmentUrls;
    private List<CommentResponse> comments;
    private Long viewCount;

    public CardResponse(Card card) {
        this.cardId = card.getId();
        this.title = card.getTitle();
        this.description = card.getDescription();
        this.deadline = card.getDeadline();
        this.status = card.getStatus();
        this.managers = card.getManagers().stream()  // 매니저 정보를 ManagerResponse로 변환
                .map(ManagerResponse::new)
                .collect(Collectors.toList());
        this.attachmentUrls = card.getAttachments().stream()
                .map(Attachment::getS3Url)
                .collect(Collectors.toList());
        this.comments = card.getComments().stream()
                .map(CommentResponse::new)
                .collect(Collectors.toList());
        this.viewCount = card.getViewCount();
    }
}
