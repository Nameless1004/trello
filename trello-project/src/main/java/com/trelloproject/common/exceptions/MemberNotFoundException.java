package com.trelloproject.common.exceptions;

public class MemberNotFoundException extends InvalidRequestException{

    public MemberNotFoundException() {
        super("존재하지 않는 멤버입니다.");
    }
}
