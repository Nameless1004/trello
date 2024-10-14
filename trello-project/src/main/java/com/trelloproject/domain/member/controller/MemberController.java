package com.trelloproject.domain.member.controller;

import com.trelloproject.common.dto.ResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MemberController {
    @PutMapping("/api/member/{memberId}")
    public ResponseDto updateMemberRole(@PathVariable Long memberId) {
        return ResponseDto.of(HttpStatus.OK, "멤버 역할 변경되었습니다.");
    }
}
