package com.gdsc.projectmiobackend.dto.request;


import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentFirstCreateRequestDto {

    @Schema(description = "댓글 내용.", example = "댓글 내용")
    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    @Schema(description = "댓글 작성일.")
    private LocalDateTime createDate;

    public Comment toEntity(Post post, UserEntity user){
        return Comment.builder()
                .content(content)
                .createDate(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();
    }
}