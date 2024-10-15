package com.trelloproject.domain.workspace.service;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.InvalidRequestException;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.domain.user.entitiy.User;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.domain.workspace.dto.WorkspaceRequest;
import com.trelloproject.domain.workspace.entity.Workspace;
import com.trelloproject.domain.workspace.repository.WorkspaceRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class WorkspaceService {
    private final WorkspaceRepository workspaceRepository;
    private final UserRepository userRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public Workspace createWorkspace(String name, String description) {
        Workspace workspace = Workspace.builder().name(name).description(description).members(List.of()).build();
        return workspaceRepository.save(workspace);
    }

    @Transactional(readOnly = true)
    public List<Workspace> getUserWorkspaces(Long userId) {
        return workspaceRepository.findAllUserWorkspaces(userId);
    }

    @Transactional
    public Workspace addMember(Long workspaceId, Long userId, MemberRole memberRole) {
        if (memberRepository.existsByWorkspace_IdAndUser_Id(workspaceId, userId))
            throw new InvalidRequestException("이미 멤버로 추가되어 있습니다.");

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new InvalidRequestException("workspaceId에 해당하는 Workspace를 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("userId에 해당하는 User를 찾을 수 없습니다."));
        Member member = Member.builder().workspace(workspace).user(user).role(memberRole).build();
        workspace.getMembers().add(member);
        return workspaceRepository.save(workspace);
    }

    public boolean hasPermissionToAddMember(Long workspaceId, WorkspaceRequest.AddMember addMemberRequestDto, AuthUser authUser) {
        if (addMemberRequestDto.memberRole().equals(MemberRole.WORKSPACE)) return false;
        Member member = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId()).orElseThrow(() -> new InvalidRequestException("해당하는 멤버가 존재하지 않습니다."));
        return member.getRole().equals(MemberRole.WORKSPACE);
    }
}
