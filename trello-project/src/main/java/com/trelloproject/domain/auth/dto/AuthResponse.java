package com.trelloproject.domain.auth.dto;

import com.trelloproject.domain.auth.dto.AuthResponse.DuplicateCheck;
import com.trelloproject.domain.auth.dto.AuthResponse.Reissue;
import com.trelloproject.domain.auth.dto.AuthResponse.Login;
import com.trelloproject.domain.auth.dto.AuthResponse.Signup;
import com.trelloproject.domain.user.entitiy.User;

public sealed interface AuthResponse permits Signup, Login, Reissue, DuplicateCheck {

    record Signup(Long userId) implements AuthResponse { }

    record Login(Long id, String userId, String userNickname, String accessToken,
                 String refreshToken) implements AuthResponse {
        public Login(User user, String access, String refresh) {
            this(user.getId(), user.getUsername(), user.getNickname(), access, refresh);
        }
    }

    record Reissue(String accessToken, String refreshToken) implements AuthResponse {}
    record DuplicateCheck(boolean isDuplicated) implements AuthResponse {}
}