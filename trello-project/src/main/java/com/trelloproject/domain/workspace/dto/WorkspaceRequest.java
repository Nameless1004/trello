package com.trelloproject.domain.workspace.dto;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest.AddMember;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest.CreateWorkspace;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;


public sealed interface WorkspaceRequest permits CreateWorkspace, AddMember {
    record CreateWorkspace(@NotBlank String name, @NotBlank String description) implements WorkspaceRequest {
    }

    record AddMember(@NotNull Long userId, @NotNull MemberRole memberRole) implements WorkspaceRequest {
    }
}
