package com.trelloproject.domain.workspace.dto;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest.AddMember;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest.CreateWorkspace;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest.UpdateWorkspace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public sealed interface WorkspaceRequest permits CreateWorkspace, AddMember, UpdateWorkspace {
    record CreateWorkspace(@NotBlank String name, @NotBlank String description) implements WorkspaceRequest {
    }

    record AddMember(@NotNull Long userId, @NotNull MemberRole memberRole) implements WorkspaceRequest {
    }

    record UpdateWorkspace(String name, String description) implements WorkspaceRequest {
    }
}
