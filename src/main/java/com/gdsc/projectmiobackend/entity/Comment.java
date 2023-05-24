package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.CommentDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Setter
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 댓글 아이디
    private Long commentId;

    // 댓글 내용
    private String content;

    // 작성일
    private LocalDateTime createDate;

    @ManyToOne
    @JoinColumn(name = "post_id")
    // 게시글 아이디
    private Post post;

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    public CommentDto toDto(){
        return CommentDto.builder()
                .content(content)
                //.like(likes)
                .createDate(createDate)
                .postId(post.getId())
                .user(user)
                .build();
    }
}
