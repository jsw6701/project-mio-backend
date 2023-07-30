package com.gdsc.projectmiobackend.controller;

import com.gdsc.projectmiobackend.dto.MannerDto;
import com.gdsc.projectmiobackend.service.MannerEntityService;
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

    @GetMapping("/manners/{userId}")
    public ResponseEntity<List<MannerDto>> getMannersByUserId(Long userId) {
        return ResponseEntity.ok(mannerEntityService.getMannersByUserId(userId));
    }
}
