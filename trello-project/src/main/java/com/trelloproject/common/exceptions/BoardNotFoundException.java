package com.trelloproject.common.exceptions;

public class BoardNotFoundException extends InvalidRequestException{

    public BoardNotFoundException() {
        super("존재하지 않는 보드입니다.");
    }
}
