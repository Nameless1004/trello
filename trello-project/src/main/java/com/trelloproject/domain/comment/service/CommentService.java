package com.trelloproject.domain.comment.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.comment.dto.CommentResponse;
import com.trelloproject.domain.comment.entity.Comment;
import com.trelloproject.domain.comment.repository.CommentRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private CommentRepository commentRepository;
    private CardRepository cardRepository;
    private MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    public ResponseDto<CommentResponse> createComment(Long cardId, Long memberId, String description) {
        // 카드 존재 확인
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        // 읽기 전용 역할 확인
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 memberId 입니다."));
        if (member.getRole().equals(MemberRole.READ_ONLY)) {
            return ResponseDto.of(HttpStatus.FORBIDDEN, "댓글을 작성할 권한이 없습니다.");
        }

        // 댓글 생성
        Comment comment = new Comment(card, member, description);
        Comment savedComment = commentRepository.save(comment);

        return ResponseDto.of(HttpStatus.CREATED, "댓글이 성공적으로 생성되었습니다.",new CommentResponse(savedComment));
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<CommentResponse> updateComment(Long cardId, Long commentId, Long memberId, String newDescription) {
        // 카드 존재 확인
        cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 commentId 입니다."));

        // 댓글 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            return ResponseDto.of(HttpStatus.FORBIDDEN, "댓글을 수정할 권한이 없습니다.");
        }

        // 댓글 수정
        comment.updateComment(newDescription);
        Comment updatedComment = commentRepository.save(comment);

        return ResponseDto.of(HttpStatus.OK, "댓글을 성공적으로 수정되었습니다.", new CommentResponse(updatedComment));
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<Void> deleteComment(Long cardId, Long commentId, Long memberId) {
        // 카드 존재 확인
        cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        // 댓글 존재 확인
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 commentId 입니다."));

        // 댓글 작성자 확인
        if (!comment.getMember().getId().equals(memberId)) {
            return ResponseDto.of(HttpStatus.FORBIDDEN, "댓글을 삭제할 권한이 없습니다.");
        }

        // 댓글 삭제
        commentRepository.delete(comment);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "댓글을 성공적으로 삭제되었습니다.");
    }
}

