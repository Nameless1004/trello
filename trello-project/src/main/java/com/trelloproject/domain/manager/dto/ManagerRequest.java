package com.trelloproject.domain.manager.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ManagerRequest {
    private Long id;
    private String name;
    private String role;
    private Long memberId;  // Member ID 포함
}

