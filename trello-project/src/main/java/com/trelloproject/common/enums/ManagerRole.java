package com.trelloproject.common.enums;

import com.trelloproject.common.exceptions.InvalidRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum ManagerRole {
    READ_ONLY, EDITOR;

    public static ManagerRole of(String role) throws InvalidRequestException {
        return Arrays.stream(ManagerRole.values())
                .filter(r -> r.name().equalsIgnoreCase(role))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 Role 입니다."));
    }
}
