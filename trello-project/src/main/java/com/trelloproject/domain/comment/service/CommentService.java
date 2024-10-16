package com.trelloproject.domain.comment.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.common.exceptions.MemberNotFoundException;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.comment.dto.CommentRequest;
import com.trelloproject.domain.comment.dto.CommentResponse;
import com.trelloproject.domain.comment.entity.Comment;
import com.trelloproject.domain.comment.repository.CommentRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class CommentService {
    private final CommentRepository commentRepository;
    private final CardRepository cardRepository;
    private final MemberRepository memberRepository;

    // 댓글 작성
    @Transactional
    public ResponseDto<CommentResponse> createComment(AuthUser authUser, Long cardId, CommentRequest commentRequest) {
        Member member = validateMemberAndCheckPermissions(authUser, cardId);
        Card card = findByIdOrThrow(cardRepository, cardId, "card");

        // 댓글 생성
        Comment comment = new Comment(card, member, commentRequest.getDescription());
        Comment savedComment = commentRepository.save(comment);

        return ResponseDto.of(HttpStatus.CREATED, "댓글이 성공적으로 생성되었습니다.", new CommentResponse(savedComment));
    }

    // 댓글 수정
    @Transactional
    public ResponseDto<CommentResponse> updateComment(AuthUser authUser, Long cardId, Long commentId, CommentRequest commentRequest) {
        Member member = validateMemberAndCheckPermissions(authUser, cardId);
        findByIdOrThrow(cardRepository, cardId, "card");

        Comment comment = findCommentByIdAndCheckOwnership(commentId, member);

        // 댓글 수정
        comment.updateComment(commentRequest.getDescription());
        Comment updatedComment = commentRepository.save(comment);

        return ResponseDto.of(HttpStatus.OK, "댓글을 성공적으로 수정되었습니다.", new CommentResponse(updatedComment));
    }

    // 댓글 삭제
    @Transactional
    public ResponseDto<Void> deleteComment(AuthUser authUser, Long cardId, Long commentId) {
        Member member = validateMemberAndCheckPermissions(authUser, cardId);
        findByIdOrThrow(cardRepository, cardId, "card");

        Comment comment = findCommentByIdAndCheckOwnership(commentId, member);

        // 댓글 삭제
        commentRepository.delete(comment);
        return ResponseDto.of(HttpStatus.OK, "댓글을 성공적으로 삭제되었습니다.");
    }

    private Member validateMemberAndCheckPermissions(AuthUser authUser, Long cardId) {
        // 카드 작성자의 권한을 확인하는 로직 (읽기 전용 멤버 제한)
        Long workspaceId = cardRepository.findWithCardListAndBoardAndWorkspaceIdByCardId(cardId);
        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if (member.getRole().equals(MemberRole.READ_ONLY)) {
            throw new AccessDeniedException("읽기 전용 멤버는 작업을 수행할 수 없습니다.");
        }

        return member;
    }

    private Comment findCommentByIdAndCheckOwnership(Long commentId, Member member) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 commentId 입니다."));

        if (!comment.getMember().getId().equals(member.getId())) {
            throw new AccessDeniedException("댓글을 수정/삭제할 권한이 없습니다.");
        }

        return comment;
    }

    private <T> T findByIdOrThrow(JpaRepository<T, Long> repository, Long id, String entityName) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 " + entityName + " ID 입니다."));
    }
}
