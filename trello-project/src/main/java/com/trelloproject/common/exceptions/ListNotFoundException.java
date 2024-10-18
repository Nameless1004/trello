package com.trelloproject.common.exceptions;

public class ListNotFoundException extends InvalidRequestException {

    public ListNotFoundException() {
        super("존재하지 않는 리스트입니다.");
    }
}
