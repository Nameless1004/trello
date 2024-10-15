package com.trelloproject.domain.workspace.dto;

import com.trelloproject.domain.workspace.dto.WorkspaceResponse.CreateWorkspace;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse.GetWorkspaces;


public sealed interface WorkspaceResponse permits CreateWorkspace, GetWorkspaces {
    record CreateWorkspace(Long id, String name, String description) implements WorkspaceResponse {
    }

    record GetWorkspaces(Long id, String name, String description) implements WorkspaceResponse {
    }
}
