package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.LogoutRequest;
import com.gdsc.projectmiobackend.dto.SocialLoginRequest;
import com.gdsc.projectmiobackend.jwt.dto.TokenResponse;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "구글 로그인")
public class AuthController {
    private final AuthService authService;


    @PostMapping("/auth/google")
    public ResponseEntity<TokenResponse> googleLogin(@RequestBody SocialLoginRequest socialLoginRequest) throws Exception {
        TokenResponse tokenResponse = authService.googleLogin(socialLoginRequest);
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "Refresh Token으로 재발급")
    @PostMapping("/reissue")
    public ResponseEntity<TokenResponse> reissue(@RequestBody LogoutRequest logoutRequest) throws Exception {
        TokenResponse tokenResponse = authService.reissue(logoutRequest.refreshToken());
        return ResponseEntity.ok(tokenResponse);
    }

    @Operation(summary = "로그아웃")
    @DeleteMapping("/user")
    public ResponseEntity<?> logout(@AuthenticationPrincipal UserInfo user, @RequestBody LogoutRequest logoutRequest) throws Exception {
        authService.logout(user.getEmail() , logoutRequest.refreshToken());
        return ResponseEntity.ok("LOGOUT_SUCCESS");
    }

    @PostMapping("/auth/google/test")
    public ResponseEntity<TokenResponse> googleTest(@RequestParam String email, @RequestParam String name) throws Exception {
        TokenResponse tokenResponse = authService.googleLoginTest(email, name);
        return ResponseEntity.ok(tokenResponse);
    }
}
