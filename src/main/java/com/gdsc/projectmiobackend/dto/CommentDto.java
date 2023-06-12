package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
@Builder
public class CommentDto {

    private Long commentId;

    private String content;

    private LocalDateTime createDate;

    private Long postId;

    private UserEntity user;

    private List<CommentDto> childComments;
}
