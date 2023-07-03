package com.gdsc.projectmiobackend.dto;


import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private LocalDate targetDate;
    private LocalTime targetTime;
    private Category category;
    private Boolean verifyGoReturn;
    private Integer numberOfPassengers;
    private UserEntity user;
    private Long viewCount;
    private Boolean verifyFinish;
    private List<UserEntity> participants;
    private Double latitude;
    private Double longitude;
    private Long bookMarkCount;
    private Long participantsCount;
    private String location;
    private Long cost;
    public PostDto(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.targetDate = post.getTargetDate();
        this.targetTime = post.getTargetTime();
        this.category = post.getCategory();
        this.verifyGoReturn = post.getVerifyGoReturn();
        this.numberOfPassengers = post.getNumberOfPassengers();
        this.viewCount = post.getViewCount();
        this.user = post.getUser();
        this.verifyFinish = post.getVerifyFinish();
        this.latitude = post.getLatitude();
        this.longitude = post.getLongitude();
        this.bookMarkCount = post.getBookMarkCount();
        this.participantsCount = post.getParticipantsCount();
        this.location = post.getLocation();
        this.cost = post.getCost();
    }
}
