package com.trelloproject.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    ROLE_WORKSPACE(Authority.WORKSPACE),
    ROLE_BOARD(Authority.BOARD),
    ROLE_READ_ONLY(Authority.READ_ONLY);

    private final String memberRole;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 MemberRole"));
    }

    public static class Authority {
        public static final String WORKSPACE = "ROLE_WORKSPACE";
        public static final String BOARD = "ROLE_BOARD";
        public static final String READ_ONLY = "ROLE_READ_ONLY";
    }
}
