package com.trelloproject.domain.member.dto;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.member.dto.MemberResponse.UpdateRole;

public sealed interface MemberResponse permits UpdateRole {
    record UpdateRole(Long id, MemberRole memberRole) implements MemberResponse {
    }
}
