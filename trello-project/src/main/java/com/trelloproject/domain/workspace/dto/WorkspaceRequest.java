package com.trelloproject.domain.workspace.dto;

import com.trelloproject.domain.workspace.dto.WorkspaceRequest.CreateWorkspace;
import jakarta.validation.constraints.NotBlank;


public sealed interface WorkspaceRequest permits CreateWorkspace {
    record CreateWorkspace(@NotBlank String name, @NotBlank String description) implements WorkspaceRequest {
    }
}
