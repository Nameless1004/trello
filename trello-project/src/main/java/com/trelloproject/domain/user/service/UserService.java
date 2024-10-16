package com.trelloproject.domain.user.service;

import com.trelloproject.common.exceptions.AccessDeniedException;
import com.trelloproject.common.exceptions.InvalidRequestException;
import com.trelloproject.common.exceptions.UserNotFoundException;
import com.trelloproject.domain.user.dto.UserRequest;
import com.trelloproject.domain.user.entity.User;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.security.AuthUser;
import com.trelloproject.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final PasswordEncoder passwordEncoder;

    public void deleteUser(long userId, AuthUser authUser, UserRequest.Delete request) {
        if(userId != authUser.getUserId()) {
            throw new AccessDeniedException("탈퇴 권한이 없습니다.");
        }

        User user = userRepository.findById(userId)
            .orElseThrow(UserNotFoundException::new);

        if(passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new AccessDeniedException("비밀번호가 다릅니다.");
        }

        if(user.isDeleted()) {
            throw new InvalidRequestException("이미 탈퇴한 유저입니다.");
        }

        user.delete();
        userRepository.save(user);

        // refresh 토큰 삭제
        redisTemplate.delete(JwtUtil.REDIS_REFRESH_TOKEN_PREFIX + authUser.getUserId());
    }
}
