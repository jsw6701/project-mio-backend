package com.gdsc.projectmiobackend.service;

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
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class AuthService {
    @Value("${google.client.id}")
    private String googleClientId;

    private final TokenProvider tokenProvider;

    // 인증 로직만 CQRS 예외
    private final UserRepository userRepository;

    @Transactional
    public TokenResponse googleLogin(String idToken) throws GeneralSecurityException, IOException {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        GoogleIdToken googleIdToken = verifier.verify(idToken);

        GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());
        return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());
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
}