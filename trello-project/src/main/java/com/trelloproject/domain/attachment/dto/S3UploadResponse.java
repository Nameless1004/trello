package com.trelloproject.domain.attachment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class S3UploadResponse {

    private String s3Url;
    private String s3Key;
}
