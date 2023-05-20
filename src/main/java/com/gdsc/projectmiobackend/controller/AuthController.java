package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.service.AuthService;
import com.gdsc.projectmiobackend.dto.LogoutRequest;
import com.gdsc.projectmiobackend.dto.SocialLoginRequest;
import com.gdsc.projectmiobackend.jwt.dto.TokenResponse;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.security.GeneralSecurityException;

@RestController
@RequiredArgsConstructor

public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) throws GeneralSecurityException, IOException {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest.token());
        return ResponseEntity.ok(tokenResponse);
    }

    @PostMapping("/token")
    public ResponseEntity<TokenResponse> reissue(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        TokenResponse tokenResponse = authService.reissue(user.getEmail(),user.getName(), logoutRequest.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @DeleteMapping("/user")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        authService.logout(user.getEmail() , logoutRequest.refreshToken());
        return ResponseEntity.ok("LOGOUT_SUCCESS");

    }
}
