package com.gdsc.projectmiobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;


@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdditionalUserPatchDto {

    @Schema(description = "성별 false: 남성 true: 여성", example = "false")
    private Boolean gender;

    @Schema(description = "흡연 여부 false: 비흡연 true: 흡연", example = "false")
    private Boolean verifySmoker;

    @Schema(description = "계좌번호", example = "국민 1234567890")
    private String accountNumber;

    @Schema(description = "활동지역", example = "서울 노원구 하계동")
    private String activityLocation;
}
