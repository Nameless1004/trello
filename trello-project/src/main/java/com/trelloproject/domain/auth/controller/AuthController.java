package com.trelloproject.domain.auth.controller;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.domain.auth.dto.AuthRequest;
import com.trelloproject.domain.auth.dto.AuthResponse.Login;
import com.trelloproject.domain.auth.service.AuthService;
import com.trelloproject.security.AuthUser;
import com.trelloproject.security.JwtUtil;
import com.trelloproject.domain.auth.dto.AuthResponse.DuplicateCheck;
import com.trelloproject.domain.auth.dto.AuthResponse.Signup;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/auth/signup")
    public ResponseEntity<ResponseDto<Signup>> signup(@Valid @RequestBody AuthRequest.Signup authRequest) {
        return ResponseDto.toEntity(authService.signup(authRequest));
    }

    @PostMapping("/auth/login")
    public ResponseEntity<ResponseDto<Login>> login(@Valid @RequestBody AuthRequest.Login authRequest) {
        return authService.login(authRequest)
            .toEntity();
    }

    @PostMapping("/auth/logout")
    public ResponseEntity<ResponseDto<Void>> logout(@AuthenticationPrincipal AuthUser user) {
        return authService.logout(user)
            .toEntity();
    }

    @PostMapping("/auth/reissue")
    public ResponseEntity<?> reissue(@RequestHeader(JwtUtil.REFRESH_TOKEN_HEADER) String refreshToken) {
        return authService.reissue(refreshToken)
            .toEntity();
    }

    @GetMapping("/auth/nickname/check")
    public ResponseEntity<ResponseDto<DuplicateCheck>> checkNickname(@RequestBody AuthRequest.CheckNickname request) {
        return authService.checkNickname(request)
            .toEntity();
    }
    @GetMapping("/auth/email/check")
    public ResponseEntity<ResponseDto<DuplicateCheck>> checkEmail(@RequestBody AuthRequest.CheckEmail request) {
        return authService.checkEmail(request)
            .toEntity();
    }
    @GetMapping("/auth/username/check")
    public ResponseEntity<ResponseDto<DuplicateCheck>> checkUsername(@RequestBody AuthRequest.CheckUsername request) {
        return authService.checkUsername(request)
            .toEntity();
    }

}
