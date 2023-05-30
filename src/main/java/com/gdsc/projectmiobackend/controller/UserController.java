package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.UserDto;
import com.gdsc.projectmiobackend.dto.request.AdditionalUserPatchDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@AllArgsConstructor
@RestController
public class UserController {

    private final AuthService authService;

    @PatchMapping("/user/{userId}")
    public ResponseEntity<UserDto> update(@PathVariable Long userId,
                                          @RequestBody AdditionalUserPatchDto additionalUserPatchDto) throws Exception {
        System.out.println("update");

        UserEntity user = authService.update(userId, additionalUserPatchDto);
        return ResponseEntity.ok(new UserDto(user));
    }

    @GetMapping("/user/id/{userId}")
    public ResponseEntity<UserDto> readUserById(@PathVariable Long userId) throws Exception {
        System.out.println("readUserById");

        UserEntity user = authService.getUserEntity(userId);
        return ResponseEntity.ok(new UserDto(user));
    }

    @GetMapping("/user/email/{userEmail}")
    public ResponseEntity<UserDto> readUserByEmail(@PathVariable String userEmail) throws Exception {
        System.out.println("readUserByEmail");

        UserEntity user = authService.getUserEntity(userEmail);
        return ResponseEntity.ok(new UserDto(user));
    }
}
