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

    @Schema(description = "성별 false: 남성 true: 여성", example = "false")
    private Boolean gender;

    @Schema(description = "흡연 여부 false: 비흡연 true: 흡연", example = "false")
    private Boolean verifySmoker;

    @Schema(description = "등/하교 선택 true 등교, false 하교", example = "true")
    private Boolean verifyGoReturn;
}
