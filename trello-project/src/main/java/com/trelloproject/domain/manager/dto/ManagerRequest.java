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

    // test 후 삭제하기
    public ManagerRequest(Long id, String name, String role, Long memberId) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.memberId = memberId;
    }
}

