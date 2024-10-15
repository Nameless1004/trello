package com.trelloproject.domain.workspace.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.UserRole;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest;
import com.trelloproject.domain.workspace.dto.WorkspaceResponse;
import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.domain.workspace.service.WorkspaceService;
import com.trelloproject.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @GetMapping("/workspaces")
    public ResponseDto<List<WorkspaceResponse.GetWorkspaces>> getWorkspaces(@AuthenticationPrincipal AuthUser authUser) {
        List<Workspace> workspaces = workspaceService.getUserWorkspaces(authUser.getUserId());
        List<WorkspaceResponse.GetWorkspaces> workspacesDtos = workspaces.stream().map(w -> new WorkspaceResponse.GetWorkspaces(w.getId(), w.getName(), w.getDescription())).toList();
        return ResponseDto.of(HttpStatus.OK, "워크스페이스 목록을 불러왔습니다.", workspacesDtos);
    }

}
