package com.trelloproject.domain.workspace.service;

import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.domain.workspace.repository.WorkspaceRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    public Workspace createWorkspace(AuthUser authUser, String name, String description) {
        return null;
    }
}
