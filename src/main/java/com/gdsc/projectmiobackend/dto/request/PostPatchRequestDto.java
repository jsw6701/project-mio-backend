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

    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @Schema(description = "위치", example = "경기 포천시 호국로 1007")
    private String location;

    @Schema(description = "비용", example = "3000")
    private Long cost;
}
