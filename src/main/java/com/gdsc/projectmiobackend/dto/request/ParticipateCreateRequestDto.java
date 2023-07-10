package com.gdsc.projectmiobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateCreateRequestDto {

    @Schema(description = "내용입니다.", example = "내용")
    @Max(value = 30, message = "30자 이내로 입력해주세요.")
    private String content;
}
