package com.trelloproject.common.exceptions;

public class UserNotFoundException extends InvalidRequestException{
    public UserNotFoundException() {
        super("존재하지 않는 유저입니다.");
    }
}
