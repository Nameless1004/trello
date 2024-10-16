package com.trelloproject.domain.auth.service;

import com.trelloproject.common.dto.ResponseDto;
import com.trelloproject.common.enums.TokenType;
import com.trelloproject.common.enums.UserRole;
import com.trelloproject.common.exceptions.AuthException;
import com.trelloproject.common.exceptions.InvalidRequestException;
import com.trelloproject.domain.auth.dto.AuthRequest;
import com.trelloproject.domain.auth.dto.AuthRequest.Login;
import com.trelloproject.domain.auth.dto.AuthResponse;
import com.trelloproject.domain.auth.dto.AuthResponse.DuplicateCheck;
import com.trelloproject.domain.user.entity.User;
import com.trelloproject.domain.user.repository.UserRepository;
import com.trelloproject.security.AuthUser;
import com.trelloproject.security.JwtUtil;
import com.trelloproject.domain.auth.dto.AuthResponse.Reissue;
import com.trelloproject.domain.auth.dto.AuthResponse.Signup;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final RedisTemplate<String, Object> redisTemplate;

    @Value("${ADMIN_TOKEN}")
    private String adminToken;

    /**
     * 회원 가입
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.Signup> signup(AuthRequest.Signup request) {
        if(!request.password().matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#$%^&*(),.?\":{}|<>])[A-Za-z\\d!@#$%^&*(),.?\":{}|<>]{8,}$")) {
            throw new InvalidRequestException("비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해야하며 최소 8글자 이상이어야 합니다.");
        }

        if(request.userRole() == UserRole.ROLE_ADMIN) {
            if( !StringUtils.hasText(request.adminToken()) || !request.adminToken().equals(adminToken)) {
                throw new AuthException("관리자 권한이 없습니다.");
            }
        }

        String username = request.username();
        String password = passwordEncoder.encode(request.password());
        String email = request.email();
        String nickname = request.nickname();

        // 회원 중복 확인
        if (userRepository.existsByUsername(username)) {
            throw new InvalidRequestException("중복된 사용자가 존재합니다.");
        }

        // email 중복확인
        if (userRepository.existsByEmail(email)) {
            throw new InvalidRequestException("중복된 Email 입니다.");
        }

        // nickname 중복확인
        if (userRepository.existsByNickname(nickname)) {
            throw new InvalidRequestException("중복된 닉네임 입니다.");
        }

        // 사용자 등록
        User user = new User(username, password, email, nickname, request.userRole());
        user = userRepository.save(user);

        return ResponseDto.of(HttpStatus.CREATED, null, new Signup(user.getId()));
    }

    /**
     * 로그인
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.Login> login(Login request) {
        User user = userRepository.findByUsername(request.username()).orElseThrow(()-> new InvalidRequestException("아이디 또는 비밀번호가 잘못되었습니다."));

        if(user.isDeleted()) {
            throw new InvalidRequestException("탈퇴한 회원입니다.");
        }

        if(!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new InvalidRequestException("아이디 또는 비밀번호가 잘못되었습니다.");
        }

        // 어드민 로그인 시 어드민 토큰 검증
        if(user.getRole() == UserRole.ROLE_ADMIN) {
            if(!StringUtils.hasText(request.adminToken())){
                throw new AuthException("관리자 권한이 없습니다.");
            }

            if(!request.adminToken().equals(adminToken)) {
                throw new AuthException("관리자 권한이 없습니다.");
            }
        }

        String accessToken = jwtUtil.createAccessToken(user.getId(), user.getEmail(), user.getRole());
        String refreshToken = jwtUtil.createRefreshToken(user.getId(), user.getEmail(), user.getRole());

        redisTemplate.opsForValue().set(JwtUtil.REDIS_REFRESH_TOKEN_PREFIX + user.getId(), refreshToken, TokenType.REFRESH.getLifeTime(), TimeUnit.MILLISECONDS);
        return ResponseDto.of(HttpStatus.OK, "성공적으로 로그인 되었습니다.",new AuthResponse.Login(user, accessToken, refreshToken));

    }

    /**
     * 로그 아웃
     * @param user
     * @return
     */
    public ResponseDto<Void> logout(AuthUser user) {
        redisTemplate.delete(JwtUtil.REDIS_REFRESH_TOKEN_PREFIX + user.getUserId());
        return ResponseDto.of(HttpStatus.OK, "로그아웃되었습니다.");
    }

    /**
     * 액세스, 리프레쉬 토큰 재발행
     * @param refreshToken
     * @return
     */
    public ResponseDto<?> reissue(String refreshToken) {

        if(refreshToken == null) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "재발급하려면 리프레쉬 토큰이 필요합니다.", null);
        }

        // 프론트에서 붙여준 Bearer prefix 제거
        try{
            refreshToken = jwtUtil.substringToken(refreshToken);
        } catch (NullPointerException e) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "잘못된 토큰 형식 입니다.", null);
        }

        // 리프레쉬 토큰인지 검사
        String category = jwtUtil.getCategory(refreshToken);
        if (!category.equals(TokenType.REFRESH.name())) {
            return ResponseDto.of(HttpStatus.BAD_REQUEST, "리프레쉬 토큰이 아닙니다.");
        }

        // 토큰 만료 검사
        try{
            jwtUtil.isExpired(refreshToken);
        } catch (ExpiredJwtException e) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }


        String key = JwtUtil.REDIS_REFRESH_TOKEN_PREFIX  + jwtUtil.getUserId(refreshToken);
        // 레디스에서 리프레쉬 토큰을 가져온다.
        refreshToken = (String) redisTemplate.opsForValue().get(key);

        if (refreshToken == null) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        // redis에서 꺼내온 리프레쉬 토큰 prefix 제거
        refreshToken = jwtUtil.substringToken(refreshToken);

        // 검증이 통과되었다면 refresh 토큰으로 액세스 토큰을 발행해준다.
        Claims claims = jwtUtil.extractClaims(refreshToken);
        Long userId = Long.parseLong(claims.getSubject());
        String email = claims.get("email", String.class);
        UserRole userRole = UserRole.of(claims.get("userRole", String.class));

        // 새 토큰 발급
        String newAccessToken = jwtUtil.createAccessToken(userId, email, userRole);
        String newRefreshToken = jwtUtil.createRefreshToken(userId, email, userRole);

        // TTL 새로해서
        String userIdToString = String.valueOf(userId);
        Long ttl = redisTemplate.getExpire(JwtUtil.REDIS_REFRESH_TOKEN_PREFIX + userIdToString, TimeUnit.MILLISECONDS);

        if(ttl == null || ttl < 0) {
            return ResponseDto.of(HttpStatus.UNAUTHORIZED, "만료된 리프레쉬 토큰입니다.", null);
        }

        redisTemplate.opsForValue().set(JwtUtil.REDIS_REFRESH_TOKEN_PREFIX  + userIdToString, newRefreshToken, ttl, TimeUnit.MILLISECONDS);

        Reissue reissue = new Reissue(newAccessToken, newRefreshToken);

        return  ResponseDto.of(HttpStatus.OK, "", reissue);
    }

    /**
     * 유저 닉네임 중복 체크
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.DuplicateCheck> checkNickname(AuthRequest.CheckNickname request) {
        DuplicateCheck duplicateCheck = new DuplicateCheck(
            userRepository.existsByNickname(request.nickname()));

        return ResponseDto.of(HttpStatus.OK, duplicateCheck);
    }

    /**
     * 유저 이메일 중복 체크
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.DuplicateCheck> checkEmail(AuthRequest.CheckEmail request) {
        DuplicateCheck duplicateCheck = new DuplicateCheck(
            userRepository.existsByEmail(request.email()));

        return ResponseDto.of(HttpStatus.OK, duplicateCheck);
    }

    /**
     * 유저 아이디 중복 체크
     * @param request
     * @return
     */
    public ResponseDto<AuthResponse.DuplicateCheck> checkUsername(AuthRequest.CheckUsername request) {
        DuplicateCheck duplicateCheck = new DuplicateCheck(
            userRepository.existsByUsername(request.username()));

        return ResponseDto.of(HttpStatus.OK, duplicateCheck);
    }
}
