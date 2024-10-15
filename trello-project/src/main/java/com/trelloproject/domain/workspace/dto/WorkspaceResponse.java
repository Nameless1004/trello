package com.trelloproject.domain.workspace.dto;

import com.trelloproject.domain.workspace.dto.WorkspaceResponse.CreateWorkspace;


public sealed interface WorkspaceResponse permits CreateWorkspace {
    record CreateWorkspace(Long id, String name, String description) implements WorkspaceResponse {
    }
}
