package com.trelloproject.domain.member.service;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;

    @Transactional
    public Member updateMemberRole(Long memberId, MemberRole memberRole) {
        Member member = memberRepository.findById(memberId).orElseThrow(() -> new IllegalArgumentException("해당하는 memberId의 멤버가 존재하지 않습니다."));
        member.setRole(memberRole);
        return memberRepository.save(member);
    }
}
