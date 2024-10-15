package com.trelloproject.domain.workspace.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.UserRole;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse;
import com.trelloproject.domain.workspace.service.WorkspaceService;
import com.trelloproject.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    @Secured(UserRole.Authority.ADMIN)
    public ResponseDto<WorkspaceResponse.CreateWorkspace> createWorkspace(@Valid @RequestBody WorkspaceRequest.CreateWorkspace createWorkspaceRequestDto, @AuthenticationPrincipal AuthUser authUser) {
        return ResponseDto.of(HttpStatus.OK);
    }
}
