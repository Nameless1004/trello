package com.trelloproject.domain.member.service;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.common.enums.UserRole;
import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import com.trelloproject.domain.user.entitiy.User;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final UserRepository userRepository;

    @Transactional
    public Member updateMemberRole(Long memberId, Long workspaceId, MemberRole memberRole, AuthUser authUser) {
        User user = userRepository.findById(authUser.getUserId()).orElseThrow(() -> new IllegalArgumentException("해당하는 userId의 유저가 존재하지 않습니다."));

        if (!user.getRole().equals(UserRole.ROLE_ADMIN)) {
            // WORKSPACE로 변경하려 하는데 유저 권한이 ADMIN이 아닐때
            if (memberRole.equals(MemberRole.WORKSPACE))
                throw new AccessDeniedException();

            Member userMember = memberRepository.findByWorkspace_IdAndUser_Id(workspaceId, user.getId()).orElseThrow();

            // WORKSPACE가 아닌 역할로 변경 하는데 멤버 권한이 WORKSPACE가 아닐때
            if (!userMember.getRole().equals(MemberRole.WORKSPACE))
                throw new AccessDeniedException();
        }

        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당하는 memberId의 멤버가 존재하지 않습니다."));
        member.setRole(memberRole);
        return memberRepository.save(member);
    }
}
