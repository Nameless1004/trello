package com.trelloproject.common.enums;

import com.trelloproject.common.exceptions.InvalidRequestException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum CardStatus {
    OPEN, ONGOING, DONE;

    public static CardStatus of(String status) throws InvalidRequestException {
        return Arrays.stream(CardStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new InvalidRequestException("유효하지 않은 Status 입니다."));
    }
}
