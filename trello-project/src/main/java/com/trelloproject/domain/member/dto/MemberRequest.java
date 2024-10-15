package com.trelloproject.domain.member.dto;

import com.trelloproject.common.enums.MemberRole;
import com.trelloproject.domain.member.dto.MemberRequest.UpdateRole;
import jakarta.validation.constraints.NotNull;

public sealed interface MemberRequest permits UpdateRole {
    record UpdateRole(@NotNull MemberRole memberRole) implements MemberRequest {
    }
}
