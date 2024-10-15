package com.trelloproject.domain.workspace.service;

import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public Workspace createWorkspace(String name, String description) {
        Workspace workspace = Workspace.builder().name(name).description(description).build();
        return workspaceRepository.save(workspace);
    }
}
