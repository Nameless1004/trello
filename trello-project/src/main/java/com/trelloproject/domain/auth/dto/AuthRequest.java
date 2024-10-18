package com.trelloproject.domain.auth.dto;

import com.trelloproject.common.enums.UserRole;
import com.trelloproject.domain.auth.dto.AuthRequest.CheckEmail;
import com.trelloproject.domain.auth.dto.AuthRequest.CheckNickname;
import com.trelloproject.domain.auth.dto.AuthRequest.CheckUsername;
import com.trelloproject.domain.auth.dto.AuthRequest.Login;
import com.trelloproject.domain.auth.dto.AuthRequest.Signup;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public sealed interface AuthRequest permits Signup, Login, CheckNickname, CheckEmail,
    CheckUsername {
    record Login(
        @NotBlank String username,
        @NotBlank String password,
        String adminToken) implements AuthRequest {}

    record Signup (
        @NotBlank String username,
        @NotBlank String password,
        @Email String email,
        @NotBlank String nickname,
        String adminToken,
        @NotNull UserRole userRole
    ) implements AuthRequest {
    }

    record CheckNickname(String nickname) implements AuthRequest {}
    record CheckEmail(String email) implements AuthRequest {}
    record CheckUsername(String username) implements AuthRequest {}
}