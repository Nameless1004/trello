package com.trelloproject.domain.comment.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.comment.dto.CommentRequest;
import com.trelloproject.domain.comment.dto.CommentResponse;
import com.trelloproject.domain.comment.service.CommentService;
import com.trelloproject.security.AuthUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/workspaces/{workspaceId}/cards/{cardId}/comments")
@RequiredArgsConstructor
public class CommentController {
    private final CommentService commentService;

    @GetMapping
    public ResponseEntity<ResponseDto<List<CommentResponse>>> getComments(
        @AuthenticationPrincipal AuthUser authUser,
        @PathVariable Long cardId) {

        return commentService.getComments(authUser, cardId).toEntity();
    }

    // 댓글 작성
    @PostMapping
    public ResponseEntity<ResponseDto<CommentResponse>> createComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cardId,
            @RequestBody CommentRequest commentRequest) {
        return commentService.createComment(authUser, cardId, commentRequest).toEntity();
    }

    // 댓글 수정
    @PutMapping("/{commentId}")
    public ResponseEntity<ResponseDto<CommentResponse>> updateComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cardId,
            @PathVariable Long commentId,
            @RequestBody CommentRequest commentRequest) {
        return commentService.updateComment(authUser, cardId, commentId, commentRequest).toEntity();
    }

    // 댓글 삭제
    @DeleteMapping("/{commentId}")
    public ResponseEntity<ResponseDto<Void>> deleteComment(
            @AuthenticationPrincipal AuthUser authUser,
            @PathVariable Long cardId,
            @PathVariable Long commentId) {
        return commentService.deleteComment(authUser, cardId, commentId).toEntity();
    }
}
