package com.trelloproject.common.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum MemberRole {
    WORKSPACE,
    BOARD,
    READ_ONLY;

    public static MemberRole of(String role) {
        return Arrays.stream(MemberRole.values())
                .filter(memberRole -> memberRole.name().equals(role))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 MemberRole"));
    }
}
