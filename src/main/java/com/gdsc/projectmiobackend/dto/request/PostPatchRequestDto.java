package com.gdsc.projectmiobackend.dto.request;

import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
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

    @Schema(description = "제목입니다.", example = "제목")
    @NotEmpty(message="제목은 필수 항목입니다.")
    private String title;

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

    @Schema(description = "동", example = "하계동")
    private String region3Depth;

    public Post toEntity(Post existingPost, Category category) {
        return Post.builder()
                .id(existingPost.getId())
                .title(this.title != null ? this.title : existingPost.getTitle())
                .content(this.content != null ? this.content : existingPost.getContent())
                .createDate(existingPost.getCreateDate())
                .targetDate(this.targetDate != null ? this.targetDate : existingPost.getTargetDate())
                .targetTime(this.targetTime != null ? this.targetTime : existingPost.getTargetTime())
                .verifyGoReturn(existingPost.getVerifyGoReturn())
                .verifySmoker(existingPost.getVerifySmoker())
                .gender(existingPost.getGender())
                .numberOfPassengers(this.numberOfPassengers != null ? this.numberOfPassengers : existingPost.getNumberOfPassengers())
                .viewCount(existingPost.getViewCount())
                .latitude(this.latitude != null ? this.latitude : existingPost.getLatitude())
                .longitude(this.longitude != null ? this.longitude : existingPost.getLongitude())
                .bookMarkCount(existingPost.getBookMarkCount())
                .participantsCount(existingPost.getParticipantsCount())
                .location(this.location != null ? this.location : existingPost.getLocation())
                .cost(this.cost != null ? this.cost : existingPost.getCost())
                .isDeleteYN(existingPost.getIsDeleteYN())
                .category(category != null ? category : existingPost.getCategory())
                .user(existingPost.getUser())
                .postType(existingPost.getPostType())
                .region3Depth(existingPost.getRegion3Depth())
                .build();
    }
}
