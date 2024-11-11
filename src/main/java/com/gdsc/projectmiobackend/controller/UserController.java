package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.UserDto;
import com.gdsc.projectmiobackend.dto.request.AdditionalUserPatchDto;
import com.gdsc.projectmiobackend.dto.request.UserAcoountApprovalStatusRequestDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.jwt.dto.UserInfo;
import com.gdsc.projectmiobackend.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
@Tag(name = "유저 정보")
public class UserController {

    private final AuthService authService;

    @Operation(summary = "유저 추가 정보 입력")
    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId,
                                          @RequestBody AdditionalUserPatchDto additionalUserPatchDto) throws Exception {

        UserEntity user = authService.update(userId, additionalUserPatchDto);
        return ResponseEntity.ok(new UserDto(user));
    }

    @Operation(summary = "아이디로 유저 정보 조회")
    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserDto> readUserById(@PathVariable Long userId) throws Exception {

        UserEntity user = authService.getUserEntity(userId);
        return ResponseEntity.ok(new UserDto(user));
    }

    @Operation(summary = "이메일로 유저 정보 조회")
    @GetMapping("/user/email/{userEmail}")
    public ResponseEntity<UserDto> readUserByEmail(@PathVariable String userEmail) throws Exception {

        UserEntity user = authService.getUserEntity(userEmail);
        return ResponseEntity.ok(new UserDto(user));
    }

    @Operation(summary = "계좌 정보 저장 승인 여부 저장")
    @PostMapping("/user/accountStatus")
    public ResponseEntity<UserDto> saveBank(@RequestBody UserAcoountApprovalStatusRequestDto requestDto, @AuthenticationPrincipal UserInfo userInfo) throws Exception {
        UserEntity user = authService.setAccountStatus(requestDto.getStatus(), userInfo);
        return ResponseEntity.ok(new UserDto(user));
    }

    @Operation(summary = "유저 정보 삭제")
    @PatchMapping("/user/delete/{userId}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable Long userId) throws Exception {
        UserEntity user = authService.delete(userId);
        return ResponseEntity.ok(new UserDto(user));
    }
}