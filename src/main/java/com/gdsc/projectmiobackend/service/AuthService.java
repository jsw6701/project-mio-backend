package com.gdsc.projectmiobackend.service;


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

    @Transactional
    public TokenResponse googleLogin(String idToken) throws Exception {
        GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new GsonFactory())
                .setAudience(Collections.singletonList(googleClientId))
                .build();

        try {
            GoogleIdToken googleIdToken = verifier.verify(idToken);

            if (googleIdToken == null) {
                throw new Exception("INVALID_TOKEN");
            }
            else {
                GoogleOAuth2UserInfo userInfo = new GoogleOAuth2UserInfo(googleIdToken.getPayload());

                if(!userInfo.getEmail().contains("@daejin.ac.kr")){
                    throw new Exception("INVALID_TOKEN");
                }


                if(!userRepository.existsByEmail(userInfo.getEmail())){
                    UserEntity userEntity = new UserEntity(userInfo);
                    userRepository.save(userEntity);
                }
                return sendGenerateJwtToken(userInfo.getEmail(), userInfo.getName());
            }
        } catch (Exception e) {
            throw new Exception("INVALID_TOKEN OR It isn't Daejin Email");
        }
    }

    @Transactional
    public UserEntity update(Long userId, AdditionalUserPatchDto additionalUserPatchDto) throws Exception {
        UserEntity userEntity = userRepository.findById(userId).orElseThrow(() -> new Exception("INVALID_TOKEN"));
        userEntity.setGender(additionalUserPatchDto.getGender());
        userEntity.setVerifySmoker(additionalUserPatchDto.getVerifySmoker());
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
}