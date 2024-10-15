package com.trelloproject.domain.workspace.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.UserRole;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse;
import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.domain.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    @Secured(UserRole.Authority.ADMIN)
    public ResponseDto<WorkspaceResponse.CreateWorkspace> createWorkspace(@Valid @RequestBody WorkspaceRequest.CreateWorkspace createWorkspaceRequestDto) {
        Workspace workspace = workspaceService.createWorkspace(createWorkspaceRequestDto.name(), createWorkspaceRequestDto.description());
        return ResponseDto.of(HttpStatus.CREATED, "워크스페이스가 생성되었습니다.", new WorkspaceResponse.CreateWorkspace(workspace.getId(), workspace.getName(), workspace.getDescription()));
    }
}
