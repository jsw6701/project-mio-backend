package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequestDto {

    @Schema(description = "제목입니다.", example = "제목")
    @NotEmpty(message="제목은 필수 항목입니다.")
    @Size(max=200)
    private String title;

    @Schema(description = "내용입니다.", example = "내용")
    @NotEmpty(message="내용은 필수 항목입니다.")
    private String content;

    @Schema(description = "타기로 한 날짜.", example = "2023-05-30")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate targetDate;

    @Schema(description = "타기로 한 시간.", example = "10:30")
    @DateTimeFormat(pattern = "HH:mm")
    private LocalTime targetTime;

    @Schema(description = "등/하교 선택 true 등교, false 하교", example = "true")
    private Boolean verifyGoReturn;

    @Schema(description = "운전자를 제외한 탑승 인원", example = "3")
    private Integer numberOfPassengers;

    @Schema(description = "조회수", example = "0")
    @Nullable
    private Long viewCount;

    @Schema(description = "도착 여부 false: 진행중 true: 도착", example = "false")
    private Boolean verifyFinish;

    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @Schema(description = "위치", example = "경기 포천시 호국로 1007")
    private String location;

    public Post toEntity(Category category, UserEntity user) {

        return Post.builder()
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .targetDate(targetDate)
                .targetTime(targetTime)
                .verifyGoReturn(verifyGoReturn)
                .numberOfPassengers(numberOfPassengers)
                .category(category)
                .viewCount(viewCount)
                .user(user)
                .verifyFinish(verifyFinish)
                .latitude(latitude)
                .longitude(longitude)
                .location(location)
                .build();
    }
}
