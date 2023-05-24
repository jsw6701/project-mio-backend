package com.gdsc.projectmiobackend.dto.request;


import com.gdsc.projectmiobackend.entity.Comment;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
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
public class CommentRequestDto {

    private Long commentId;

    @NotEmpty(message = "내용은 필수 항목입니다.")
    private String content;

    private LocalDateTime createDate;

    private Long postId;

    public Comment toEntity(Post post, UserEntity user){
        return Comment.builder()
                .commentId(commentId)
                .content(content)
                .createDate(LocalDateTime.now())
                .post(post)
                .user(user)
                .build();
    }
}
