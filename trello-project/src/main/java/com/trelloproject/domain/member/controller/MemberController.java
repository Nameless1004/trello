package com.trelloproject.domain.member.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.member.dto.MemberRequest;
import com.trelloproject.domain.member.dto.MemberResponse;
import com.trelloproject.domain.member.entity.Member;
import com.trelloproject.domain.member.service.MemberService;
import com.trelloproject.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @PutMapping("/workspace/{workspaceId}/member/{memberId}")
    public ResponseDto<MemberResponse.UpdateRole> updateMemberRole(@PathVariable Long memberId, @PathVariable Long workspaceId, @Valid @RequestBody MemberRequest.UpdateRole updateRoleRequest, @AuthenticationPrincipal AuthUser authUser) {
        Member member = memberService.updateMemberRole(memberId, workspaceId, updateRoleRequest.memberRole(), authUser);
        return ResponseDto.of(HttpStatus.OK, "멤버 역할이 변경되었습니다.", new MemberResponse.UpdateRole(member.getId(), member.getRole()));
    }
}
