package com.trelloproject.domain.attachment.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.attachment.dto.AttachmentResponse;
import com.trelloproject.domain.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/cards/{cardId}/files")
@RequiredArgsConstructor
public class AttachmentController {
    private final AttachmentService attachmentService;

    // 첨부파일 추가
    @PutMapping
    public ResponseEntity<ResponseDto<AttachmentResponse>> saveFiles(
            @PathVariable Long cardId,
            @RequestPart(name = "file") MultipartFile file) throws IOException {
        return attachmentService.saveFile(cardId, file).toEntity();
    }

    // 첨부파일 조회
    @GetMapping
    public ResponseEntity<ResponseDto<List<AttachmentResponse>>> getFiles(
            @PathVariable Long cardId) {
        return attachmentService.getFiles(cardId).toEntity();
    }

    // 첨부파일 삭제
    @DeleteMapping("/{fileId}")
    public ResponseEntity<ResponseDto<Void>> deleteFile(
            @PathVariable Long cardId,
            @PathVariable Long fileId) {
        return attachmentService.deleteFile(cardId, fileId).toEntity();
    }
}
