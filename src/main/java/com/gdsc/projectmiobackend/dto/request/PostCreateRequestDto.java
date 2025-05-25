package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.common.PostType;
import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

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

    @Schema(description = "성별 false: 남성 true: 여성", example = "false")
    private Boolean gender;

    @Schema(description = "흡연 여부 false: 비흡연 true: 흡연", example = "false")
    private Boolean verifySmoker;

    @Schema(description = "등/하교 선택 true 등교, false 하교", example = "true")
    private Boolean verifyGoReturn;

    @Schema(description = "운전자를 제외한 탑승 인원", example = "3")
    private Integer numberOfPassengers;

    @Schema(description = "위도", example = "37.123456")
    private Double latitude;

    @Schema(description = "경도", example = "127.123456")
    private Double longitude;

    @Schema(description = "위치", example = "경기 포천시 호국로 1007")
    private String location;

    @Schema(description = "요금", example = "3000")
    private Long cost;

    @Schema(description = "동", example = "하계동")
    private String region3Depth;

    public Post toEntity(UserEntity user, Category category){
        return Post.builder()
                .category(category)
                .title(title)
                .content(content)
                .targetDate(targetDate)
                .targetTime(targetTime)
                .verifyGoReturn(verifyGoReturn)
                .gender(gender)
                .verifySmoker(verifySmoker)
                .numberOfPassengers(numberOfPassengers)
                .participantsCount(0L)
                .viewCount(0L)
                .latitude(latitude)
                .longitude(longitude)
                .location(location)
                .cost(cost)
                .isDeleteYN("N")
                .createDate(LocalDateTime.now())
                .user(user)
                .postType(PostType.BEFORE_DEADLINE)
                .bookMarkCount(0L)
                .region3Depth(region3Depth)
                .build();
    }
}
