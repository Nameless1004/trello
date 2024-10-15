package com.trelloproject.domain.member.dto;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.member.dto.MemberResponse.MemberWithUser;
import com.trelloproject.domain.member.dto.MemberResponse.UpdateRole;
import com.trelloproject.domain.member.entity.Member;

public sealed interface MemberResponse permits UpdateRole, MemberWithUser {
    record UpdateRole(Long id, MemberRole memberRole) implements MemberResponse {
    }

    record MemberWithUser(Long id, MemberRole memberRole, Long userId, String nickname) implements MemberResponse {
        public MemberWithUser(Member m) {
            this(m.getId(), m.getRole(), m.getUser().getId(), m.getUser().getNickname());
        }
    }
}
