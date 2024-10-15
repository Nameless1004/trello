package com.trelloproject.common.exceptions;

public class WorkspaceNotFounException extends RuntimeException {
    public WorkspaceNotFounException() {
        super("존재하지 않는 워크스페이스 입니다.");
    }
}
