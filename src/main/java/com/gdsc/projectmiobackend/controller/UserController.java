package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.UserDto;
import com.gdsc.projectmiobackend.dto.request.AdditionalUserPatchDto;
import com.gdsc.projectmiobackend.entity.UserEntity;
import com.gdsc.projectmiobackend.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

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
}
