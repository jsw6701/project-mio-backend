package com.gdsc.projectmiobackend.service;


import com.gdsc.projectmiobackend.discord.MsgService;
import com.gdsc.projectmiobackend.dto.SocialLoginRequest;
import com.gdsc.projectmiobackend.dto.request.AdditionalUserPatchDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.repository.UserRepository;
import com.gdsc.projectmiobackend.common.RoleType;
import com.gdsc.projectmiobackend.jwt.TokenProvider;
import com.gdsc.projectmiobackend.jwt.dto.TokenResponse;
import com.gdsc.projectmiobackend.oauth.GoogleOAuth2UserInfo;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.google.api.client.json.gson.GsonFactory;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${google.client.id}")
    private String googleClientId;

    private final TokenProvider tokenProvider;

    // 인증 로직만 CQRS 예외
    private final UserRepository userRepository;

    private final MsgService msgService;

    @Transactional
    public TokenResponse googleLogin(SocialLoginRequest socialLoginRequest) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(socialLoginRequest.token());

            if (googleIdToken == null) {
                msgService.sendMsg("구글 로그인 에러", socialLoginRequest.method() + " " + socialLoginRequest.url(), "Token ID NULL");
                throw new Exception("INVALID_TOKEN");
            }
            else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                if(!userInfo.getEmail().contains("@daejin.ac.kr")){
                    msgService.sendMsg("구글 로그인 에러", socialLoginRequest.method() + " " + socialLoginRequest.url(), "대진대학교 이메일이 아닙니다.");
                    throw new Exception("INVALID_TOKEN");
                }


                if(!userRepository.existsByEmail(userInfo.getEmail())){
                    UserEntity userEntity = new UserEntity(userInfo);
                    msgService.sendMsg("신규 유저 등록", socialLoginRequest.method() + " " + socialLoginRequest.url(), "신규 유저 등록 : " + userInfo.getEmail() + " / " + userInfo.getName());
                    userRepository.save(userEntity);
                }
                else{
                    msgService.sendMsg("유저 로그인", socialLoginRequest.method() + " " + socialLoginRequest.url(), "유저 로그인 : " + userInfo.getEmail() + " / " + userInfo.getName());
                }
                return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());
            }
        } catch (Exception e) {
            msgService.sendMsg("구글 로그인 에러", socialLoginRequest.method() + " " + socialLoginRequest.url(), e.getMessage());
            throw new Exception("대진대학교 이메일이 아니거나 잘못된 토큰 값입니다.");
        }

    }

    @Transactional
    public UserEntity update(Long userId, AdditionalUserPatchDto additionalUserPatchDto) throws Exception {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new Exception("INVALID_TOKEN"));
        userEntity.setGender(additionalUserPatchDto.getGender());
        userEntity.setVerifySmoker(additionalUserPatchDto.getVerifySmoker());
        userEntity.setAccountNumber(additionalUserPatchDto.getAccountNumber());
        return userEntity;
    }

    @Transactional
    public void logout(String email, String refreshToken) throws Exception {
        validateRefreshToken(refreshToken);
        Claims claims = tokenProvider.parseClaims(refreshToken);
    }

    @Transactional
    public TokenResponse reissue(String email, String name, String refreshToken) throws Exception {
        validateRefreshToken(refreshToken);

        TokenResponse tokenResponse = createToken(email, name);
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
}