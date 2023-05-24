package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;

    private String content;

    private Integer like;

    private LocalDateTime createDate;

    private Long postId;

    private UserEntity user;
}
