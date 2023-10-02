package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.MannerDto;
import com.gdsc.projectmiobackend.service.MannerEntityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@AllArgsConstructor
@RestController
@Tag(name = "매너 저장 컨트롤러")
public class MannerController {

    private final MannerEntityService mannerEntityService;

    @Operation(summary = "유저가 받은 매너 조회", description = "유저가 받은 매너를 조회합니다.")
    @GetMapping("/manners/get/{userId}")
    public ResponseEntity<List<MannerDto>> findMannersByGetUserId(Long userId) {
        return ResponseEntity.ok(mannerEntityService.getMannersByPostUserId(userId));
    }

    @Operation(summary = "유저가 작성한 매너 조회", description = "유저가 작성한 매너를 조회합니다.")
    @GetMapping("/manners/post/{userId}")
    public ResponseEntity<List<MannerDto>> findMannersByPostUserId(Long userId) {
        return ResponseEntity.ok(mannerEntityService.getMannersByGetUserId(userId));
    }
}
