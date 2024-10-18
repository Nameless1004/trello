package com.trelloproject.common.exceptions;

public class CardNotFoundException extends RuntimeException {
    public CardNotFoundException() {
        super("존재하지 않는 카드입니다.");
    }
}
