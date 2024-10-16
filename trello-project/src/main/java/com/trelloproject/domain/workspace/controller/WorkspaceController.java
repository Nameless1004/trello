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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {
    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    @Secured(UserRole.Authority.ADMIN)
    public ResponseDto<WorkspaceResponse.WorkspaceWithMember> createWorkspace(@Valid @RequestBody WorkspaceRequest.CreateWorkspace createWorkspaceRequestDto) {
        Workspace workspace = workspaceService.createWorkspace(createWorkspaceRequestDto.name(), createWorkspaceRequestDto.description());
        return ResponseDto.of(HttpStatus.CREATED, "워크스페이스가 생성되었습니다.", new WorkspaceResponse.WorkspaceWithMember(workspace.getMembers(), workspace.getId(), workspace.getName(), workspace.getDescription()));
    }

    @GetMapping("/workspaces")
    public ResponseDto<List<WorkspaceResponse.Workspace>> getWorkspaces(@AuthenticationPrincipal AuthUser authUser) {
        List<Workspace> workspaces = workspaceService.getUserWorkspaces(authUser.getUserId());
        List<WorkspaceResponse.Workspace> workspacesDtos = workspaces.stream().map(w -> new WorkspaceResponse.Workspace(w.getId(), w.getName(), w.getDescription())).toList();
        return ResponseDto.of(HttpStatus.OK, "워크스페이스 목록을 불러왔습니다.", workspacesDtos);
    }

    @GetMapping("/workspaces/{workspaceId}")
    @PreAuthorize("hasRole(T(com.trelloproject.common.enums.UserRole.Authority).ADMIN) or @workspaceService.hasPermissionToGetWorkspace(#workspaceId, #authUser)")
    public ResponseDto<WorkspaceResponse.WorkspaceWithMember> getWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal AuthUser authUser) {
        Workspace workspace = workspaceService.getWorkspace(workspaceId);
        return ResponseDto.of(HttpStatus.OK, "워크스페이스를 불러왔습니다.", new WorkspaceResponse.WorkspaceWithMember(workspace.getMembers(), workspace.getId(), workspace.getName(), workspace.getDescription()));
    }

    @PostMapping("/workspaces/{workspaceId}/members")
    @PreAuthorize("hasRole(T(com.trelloproject.common.enums.UserRole.Authority).ADMIN) or @workspaceService.hasPermissionToAddMember(#workspaceId, #addMemberRequestDto, #authUser)")
    public ResponseDto<WorkspaceResponse.WorkspaceWithMember> addMember(@PathVariable Long workspaceId, @Valid @RequestBody WorkspaceRequest.AddMember addMemberRequestDto, @AuthenticationPrincipal AuthUser authUser) {
        Workspace workspace = workspaceService.addMember(workspaceId, addMemberRequestDto.userId(), addMemberRequestDto.memberRole());
        return ResponseDto.of(HttpStatus.CREATED, "워크스페이스에 멤버가 추가되었습니다.", new WorkspaceResponse.WorkspaceWithMember(workspace.getMembers(), workspace.getId(), workspace.getName(), workspace.getDescription()));
    }

    @DeleteMapping("/workspaces/{workspaceId}/members/{memberId}")
    @PreAuthorize("hasRole(T(com.trelloproject.common.enums.UserRole.Authority).ADMIN) or @workspaceService.hasPermissionToDeleteMember(#workspaceId, #memberId, #authUser)")
    public ResponseDto deleteMember(@PathVariable Long workspaceId, @PathVariable Long memberId, @AuthenticationPrincipal AuthUser authUser) {
        workspaceService.deleteMember(workspaceId, memberId);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "멤버가 삭제되었습니다.");
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    @PreAuthorize("hasRole(T(com.trelloproject.common.enums.UserRole.Authority).ADMIN) or @workspaceService.hasPermissionToModifyWorkspace(#workspaceId, #authUser)")
    public ResponseDto deleteWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal AuthUser authUser) {
        workspaceService.deleteWorkspace(workspaceId);
        return ResponseDto.of(HttpStatus.NO_CONTENT, "워크스페이스가 삭제되었습니다.");
    }

    @PutMapping("/workspaces/{workspaceId}")
    @PreAuthorize("hasRole(T(com.trelloproject.common.enums.UserRole.Authority).ADMIN) or @workspaceService.hasPermissionToModifyWorkspace(#workspaceId, #authUser)")
    public ResponseDto<WorkspaceResponse.Workspace> updateWorkspace(@PathVariable Long workspaceId, @AuthenticationPrincipal AuthUser authUser, @Valid @RequestBody WorkspaceRequest.UpdateWorkspace updateWorkspaceRequestDto) {
        Workspace workspace = workspaceService.updateWorkspace(workspaceId, updateWorkspaceRequestDto.name(), updateWorkspaceRequestDto.description());
        return ResponseDto.of(HttpStatus.OK, "워크스페이스가 수정되었습니다.", new WorkspaceResponse.Workspace(workspace.getId(), workspace.getName(), workspace.getDescription()));
    }
}
