package com.trelloproject.domain.attachment.dto;

import com.trelloproject.domain.attachment.entity.Attachment;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class AttachmentResponse {
    private Long id;
    private String s3Url;

    public AttachmentResponse(Attachment attachment) {
        this.id = attachment.getId();
        this.s3Url = attachment.getS3Url();
    }
}
