package com.trelloproject.domain.workspace.service;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.InvalidRequestException;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.domain.user.entity.User;
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

    @Transactional(readOnly = true)
    public Workspace getWorkspace(Long workspaceId) {
        return workspaceRepository.findByIdWithMember(workspaceId).orElseThrow(() -> new InvalidRequestException("workspaceId에 해당하는 Workspace를 찾을 수 없습니다."));
    }

    @Transactional
    public Workspace addMember(Long workspaceId, Long userId, MemberRole memberRole) {
        if (memberRepository.existsByWorkspace_IdAndUser_Id(workspaceId, userId))
            throw new InvalidRequestException("이미 멤버로 추가되어 있습니다.");

        Workspace workspace = workspaceRepository.findById(workspaceId).orElseThrow(() -> new InvalidRequestException("workspaceId에 해당하는 Workspace를 찾을 수 없습니다."));
        User user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("userId에 해당하는 User를 찾을 수 없습니다."));
        Member member = Member.builder().workspace(workspace).user(user).role(memberRole).deleted(false).build();
        workspace.getMembers().add(member);
        return workspaceRepository.save(workspace);
    }

    @Transactional
    public void deleteMember(Long workspaceId, Long memberId) {
        if (!memberRepository.existsByWorkspace_IdAndId(workspaceId, memberId))
            throw new InvalidRequestException("해당하는 Member를 찾을 수 없습니다.");

        memberRepository.deleteById(memberId);
    }

    // 유저 역할 ADMIN이 아닌 유저가 멤버 추가하려 할 때
    // 추가하려는 멤버의 역할이 WORKSPACE이면 -> 멤버 추가 권한 없음 (ADMIN만 WORKSPACE로 추가 가능함)
    // 현제 유저의 멤버 역할이 WORKSPACE이면 -> 멤버 추가 권한 있음
    public boolean hasPermissionToAddMember(Long workspaceId, WorkspaceRequest.AddMember addMemberRequestDto, AuthUser authUser) {
        if (addMemberRequestDto.memberRole().equals(MemberRole.WORKSPACE)) return false;

        try {
            Member authMember = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId()).orElseThrow(() -> new InvalidRequestException("해당하는 멤버가 존재하지 않습니다."));
            return authMember.getRole().equals(MemberRole.WORKSPACE);
        } catch (InvalidRequestException e) {
            return false;
        }
    }

    // 유저 역할 ADMIN이 아닌 유저가 멤버 삭제하려 할 때
    // 삭제하려는 멤버의 역할이 WORKSPACE이면 -> 멤버 삭제 권한 없음 (ADMIN만 WORKSPACE 멤버 삭제 가능함)
    // 현제 유저의 멤버 역할이 WORKSPACE이면 -> 멤버 삭제 권한 있음
    public boolean hasPermissionToDeleteMember(Long workspaceId, Long memberId, AuthUser authUser) {
        try {
            Member member = memberRepository.findByWorkspace_IdAndId(workspaceId, memberId).orElseThrow(() -> new InvalidRequestException("해당하는 멤버가 존재하지 않습니다."));
            if (member.getRole().equals(MemberRole.WORKSPACE)) return false;

            Member authMember = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId()).orElseThrow(() -> new InvalidRequestException("해당하는 멤버가 존재하지 않습니다."));
            return authMember.getRole().equals(MemberRole.WORKSPACE);
        } catch (InvalidRequestException e) {
            return false;
        }
    }

    // 워크스페이스 가져올 수 있는지 권한 확인
    public boolean hasPermissionToGetWorkspace(Long workspaceId, AuthUser authUser) {
        return memberRepository.existsByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId());
    }
}
