package com.trelloproject.domain.user.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.user.dto.UserRequest;
import com.trelloproject.domain.user.service.UserService;
import com.trelloproject.security.AuthUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @DeleteMapping("/api/users/{userId}")
    public ResponseEntity<ResponseDto<Void>> deleteUsers(
        @PathVariable("userId") long userId,
        @AuthenticationPrincipal AuthUser authUser,
        @Valid @RequestBody UserRequest.Delete request) {
        userService.deleteUser(userId, authUser, request);
        return ResponseDto.toEntity(ResponseDto.of(HttpStatus.OK, "회원탈퇴되었습니다."));
    }
}
