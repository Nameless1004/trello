package com.trelloproject.domain.comment.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.comment.dto.CommentResponse;
import com.trelloproject.domain.comment.service.CommentService;
import com.trelloproject.domain.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ResponseDto<CommentResponse>> createComment(
            @PathVariable Long cardId,
            @RequestBody String description,
            @AuthenticationPrincipal Member member) {
        return commentService.createComment(cardId, member.getId(), description).toEntity();
    }

    // 댓글 수정
    @PatchMapping("/{commentId}")
    public ResponseEntity<ResponseDto<CommentResponse>> updateComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            @RequestBody String newDescription,
            @AuthenticationPrincipal Member member) {
        return commentService.updateComment(cardId, commentId, member.getId(), newDescription).toEntity();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            @AuthenticationPrincipal Member member) {
        return commentService.deleteComment(cardId, commentId, member.getId()).toEntity();
    }
}
