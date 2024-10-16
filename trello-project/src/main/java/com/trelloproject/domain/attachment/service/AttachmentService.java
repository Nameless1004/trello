package com.trelloproject.domain.attachment.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.common.exceptions.MemberNotFoundException;
import com.trelloproject.common.service.S3Service;
import com.trelloproject.domain.attachment.dto.AttachmentResponse;
import com.trelloproject.domain.attachment.dto.S3UploadResponse;
import com.trelloproject.domain.attachment.entity.Attachment;
import com.trelloproject.domain.attachment.repository.AttachmentRepository;
import com.trelloproject.domain.card.entity.Card;
import com.trelloproject.domain.card.repository.CardRepository;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AttachmentService {
    private final CardRepository cardRepository;
    private final S3Service s3Service;
    private final AttachmentRepository attachmentRepository;
    private final MemberRepository memberRepository;

    // 지원되는 파일 형식과 크기 제한
    private static final List<String> SUPPORTED_FILE_TYPES = Arrays.asList("image/jpeg", "image/png", "application/pdf", "text/csv");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    // 파일 저장
    @Transactional
    public ResponseDto<AttachmentResponse> saveFile(AuthUser authUser, Long cardId, MultipartFile file) throws IOException {
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if(member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 생성할 수 없습니다.");
        }

        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        // 파일 형식 및 크기 검사
        if (!SUPPORTED_FILE_TYPES.contains(file.getContentType())) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "지원되지 않는 파일 형식입니다.");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "파일 크기가 5MB를 초과합니다.");
        }

        // 파일 저장
        S3UploadResponse s3UploadResponse = s3Service.uploadFile(file);
        Attachment attachment = new Attachment(s3UploadResponse.getS3Url(), s3UploadResponse.getS3Key(), card);
        attachmentRepository.save(attachment);
        AttachmentResponse response = new AttachmentResponse(attachment);

        return ResponseDto.of(HttpStatus.OK, "파일이 성공적으로 업로드되었습니다.", response);
    }

    // 첨부파일 조회
    public ResponseDto<List<AttachmentResponse>> getFiles(Long cardId) {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 cardId 입니다."));

        List<Attachment> attachments = attachmentRepository.findAllByCard(card);
        List<AttachmentResponse> response = attachments.stream()
                .map(AttachmentResponse::new)
                .collect(Collectors.toList());

        return ResponseDto.of(HttpStatus.OK, "첨부파일 조회가 성공되었습니다.", response);
    }

    // 첨부파일 삭제
    @Transactional
    public ResponseDto<Void> deleteFile(AuthUser authUser, Long cardId, Long fileId) {
        Member member = memberRepository.findByUserId(authUser.getUserId())
                .orElseThrow(MemberNotFoundException::new);

        if(member.getRole() == MemberRole.READ_ONLY) {
            throw new AccessDeniedException("읽기 전용 멤버는 삭제할 수 없습니다.");
        }

        if (!cardRepository.existsById(cardId)) {
            throw new IllegalArgumentException("잘못된 cardId 입니다.");
        }
        Attachment attachment = attachmentRepository.findById(fileId)
                .orElseThrow(() -> new IllegalArgumentException("잘못된 파일 ID 입니다."));
        
        // s3key 있으면 s3에서 삭제
        if(StringUtils.hasText(attachment.getS3Key())) {
            s3Service.deleteFile(attachment.getS3Key());
        }
        attachmentRepository.delete(attachment);

        return ResponseDto.of(HttpStatus.NO_CONTENT, "파일이 성공적으로 삭제되었습니다.");
    }
}
