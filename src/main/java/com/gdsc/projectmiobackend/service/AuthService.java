package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.common.AccountApprovalStatus;
import com.gdsc.projectmiobackend.common.ErrorCode;
import com.gdsc.projectmiobackend.common.RoleType;
import com.gdsc.projectmiobackend.common.Status;
import com.gdsc.projectmiobackend.discord.MsgService;
import com.gdsc.projectmiobackend.dto.SocialLoginRequest;
import com.gdsc.projectmiobackend.dto.request.AdditionalUserPatchDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.exception.CustomException;
import com.gdsc.projectmiobackend.jwt.TokenProvider;
import com.gdsc.projectmiobackend.jwt.dto.TokenResponse;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.oauth.GoogleOAuth2UserInfo;
import com.gdsc.projectmiobackend.repository.UserRepository;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${google.client.debug.id}")
    private String googleClientDebugId;

    @Value("${google.client.release.id}")
    private String googleClientReleaseId;

    private final TokenProvider tokenProvider;

    // 인증 로직만 CQRS 예외
    private final UserRepository userRepository;

    private final MsgService msgService;

    private final RedisTemplate<String, String> redisTemplate;

    @Transactional
    public TokenResponse googleLogin(SocialLoginRequest socialLoginRequest) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Arrays.asList(googleClientDebugId, googleClientReleaseId))
                .build();


        GoogleIdToken googleIdToken;
        try {
            googleIdToken = verifier.verify(socialLoginRequest.token());
        } catch (IllegalArgumentException e) {
            throw new CustomException(ErrorCode.INVALID_TOKEN_VALUE);
        }

        if (googleIdToken == null) {
            throw new CustomException(ErrorCode.UNAUTHORIZED);
        }
        else {
            GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

//            if(userInfo.getEmail().contains("@daejin.ac.kr") || userInfo.getEmail().contains("anes53027@")){
            if(!userRepository.existsByEmail(userInfo.getEmail())){
                UserEntity userEntity = new UserEntity(userInfo);
                msgService.sendMsg("유저 로그인", userInfo.getEmail() + " / " + userInfo.getName(), "새 유저 생성");
                userRepository.save(userEntity);
            }
            else{
                UserEntity userEntity = userRepository.findByEmail(userInfo.getEmail()).orElseThrow(() -> new CustomException(ErrorCode.NOT_FOUND));
                if(userEntity.getStatus().equals(Status.SUSPEND)){
                    throw new CustomException(ErrorCode.SUSPEND_USER);
                }

                msgService.sendMsg("유저 로그인", userInfo.getEmail() + " / " + userInfo.getName(), "기존 유저 로그인");
            }
            TokenResponse tokenResponse = sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());

            // Refresh Token 저장
            saveRefreshToken(userInfo.getEmail(), tokenResponse.refreshToken());

            return tokenResponse;
//            }
/*            else{
                throw new Exception("대진대학교 이메일이 아니거나, 권한이 없습니다.");
            }*/
        }

    }

    @Transactional
    public TokenResponse googleLoginTest(String email, String name) throws Exception {
        TokenResponse tokenResponse = sendGenerateJwtToken(email, name);
        // Refresh Token 저장
        saveRefreshToken(email, tokenResponse.refreshToken());

        return tokenResponse;
    }

    @Transactional
    public UserEntity update(Long userId, AdditionalUserPatchDto additionalUserPatchDto) throws Exception {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new Exception("INVALID_TOKEN"));
        userEntity.setGender(additionalUserPatchDto.getGender());
        userEntity.setVerifySmoker(additionalUserPatchDto.getVerifySmoker());
        userEntity.setAccountNumber(additionalUserPatchDto.getAccountNumber());
        userEntity.setActivityLocation(additionalUserPatchDto.getActivityLocation());
        return userEntity;
    }

    @Transactional
    public UserEntity delete(Long userId) throws Exception {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new Exception("INVALID_TOKEN"));
        userEntity.setAccountNumber("(알 수 없음)");
        userEntity.setActivityLocation("(알 수 없음)");
        userEntity.setVerifySmoker(false);
        userEntity.setGender(false);
        userEntity.setMannerCount(0L);
        userEntity.setName("(알 수 없음)");
        userEntity.setProfileImageUrl("(알 수 없음)");
        userEntity.setRoleType(RoleType.MEMBER);
        userEntity.setStatus(Status.SUSPEND);
        userEntity.setStudentId("(알 수 없음)");
        userEntity.setGrade("(알 수 없음)");

        userRepository.save(userEntity);
        return userEntity;
    }

    @Transactional
    public void logout(String email, String refreshToken) throws Exception {
        // Redis에서 이메일로 저장된 리프레시 토큰 조회
        String storedToken = getRefreshToken(email);
        if (storedToken != null && storedToken.equals(refreshToken)) {
            deleteRefreshToken(email); // 이메일로 리프레시 토큰 삭제
        }
    }

    @Transactional
    public TokenResponse reissue(String refreshToken) throws Exception {
        // Redis에서 모든 이메일-리프레시 토큰 데이터 조회
        String email = redisTemplate.opsForHash().entries("refreshTokens").entrySet().stream()
                .filter(entry -> refreshToken.equals(entry.getValue()))
                .map(entry -> (String) entry.getKey())
                .findFirst()
                .orElseThrow(() -> new Exception("Invalid Refresh Token"));

        // 이메일로 유저 정보 조회
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("User not found"));

        // 새로운 Access Token 및 Refresh Token 생성
        TokenResponse tokenResponse = tokenProvider.generateJwtToken(email, userEntity.getName(), userEntity.getRoleType());

        // Redis에 새로운 Refresh Token 저장 (기존 토큰은 무효화)
        saveRefreshToken(email, tokenResponse.refreshToken());

        return tokenResponse;
    }

    private TokenResponse sendGenerateJwtToken(String email, String name) {
        TokenResponse tokenResponse = createToken(email, name);
        return tokenResponse;
    }

    private void validateRefreshToken(String refreshToken) throws Exception {
        if(!tokenProvider.validateToken(refreshToken))
            throw new Exception("validateRefreshTokenError");
    }

    private TokenResponse createToken(String email, String name) {
        return tokenProvider.generateJwtToken(email, name, RoleType.MEMBER);
    }

    @Transactional
    public UserEntity getUserEntity(String email) throws Exception {
        return userRepository.findByEmail(email).orElseThrow(() -> new Exception("INVALID_TOKEN"));
    }

    @Transactional
    public UserEntity getUserEntity(Long userId) throws Exception {
        return userRepository.findById(userId).orElseThrow(() -> new Exception("INVALID_TOKEN"));
    }

    public void saveRefreshToken(String email, String refreshToken) {
        redisTemplate.opsForHash().put("refreshTokens", email, refreshToken);
        redisTemplate.expire("refreshTokens", 7, TimeUnit.DAYS); // 전체 키에 만료 시간 설정
    }

    // Redis에서 이메일로 리프레시 토큰 조회
    public String getRefreshToken(String email) {
        return (String) redisTemplate.opsForHash().get("refreshTokens", email);
    }

    // Redis에서 이메일로 리프레시 토큰 삭제
    public void deleteRefreshToken(String email) {
        redisTemplate.opsForHash().delete("refreshTokens", email);
    }

    public UserEntity setAccountStatus(AccountApprovalStatus status, UserInfo user) throws Exception {
        UserEntity userEntity = userRepository.findByEmail(user.getEmail()).orElseThrow();
        userEntity.setAccountApprovalStatus(status);
        userRepository.save(userEntity);
        return userEntity;
    }
}