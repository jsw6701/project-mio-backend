package com.gdsc.projectmiobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostVerifyFinishRequestDto {
    @Schema(description = "도착 여부 false: 진행중 true: 도착", example = "false")
    private Boolean verifyFinish;
}
