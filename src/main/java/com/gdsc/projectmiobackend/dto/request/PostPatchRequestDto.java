package com.gdsc.projectmiobackend.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostPatchRequestDto {

    @Schema(description = "내용입니다.", example = "내용")
    @NotEmpty(message="내용은 필수 항목입니다.")
    private String content;

    @Schema(description = "카테고리 ID", example = "1")
    private Long categoryId;

    @Schema(description = "타기로 한 날짜.", example = "2023-05-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @Schema(description = "타기로 한 시간.", example = "10:30")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime targetTime;

    @Schema(description = "운전자를 제외한 탑승자 수", example = "3")
    private Integer numberOfPassengers;

    @Schema(description = "도착 여부 false: 진행중 true: 도착", example = "false")
    private Boolean verifyFinish;
}
