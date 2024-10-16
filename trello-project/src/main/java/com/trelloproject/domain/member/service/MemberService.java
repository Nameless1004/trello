package com.trelloproject.domain.member.service;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.exceptions.InvalidRequestException;
import com.trelloproject.domain.member.dto.MemberRequest;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member updateMemberRole(Long memberId, MemberRole memberRole) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new InvalidRequestException("해당하는 memberId의 멤버가 존재하지 않습니다."));
        member.setRole(memberRole);
        return memberRepository.save(member);
    }

    public boolean hasPermissionToUpdateRole(Long workspaceId, MemberRequest.UpdateRole updateRoleRequest, AuthUser authUser) {
        // WORKSPACE로 변경하려 하는데 유저 권한이 ADMIN이 아닐때
        if (updateRoleRequest.memberRole().equals(MemberRole.WORKSPACE)) return false;

        try {
            Member userMember = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, authUser.getUserId()).orElseThrow(() -> new InvalidRequestException("해당하는 멤버가 존재하지 않습니다."));
            // WORKSPACE가 아닌 역할로 변경 하는데 멤버 권한이 WORKSPACE가 아닐때
            return userMember.getRole().equals(MemberRole.WORKSPACE);
        } catch (Exception e) {
            return false;
        }
    }
}
