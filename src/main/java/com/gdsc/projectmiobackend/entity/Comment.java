package com.gdsc.projectmiobackend.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gdsc.projectmiobackend.dto.CommentDto;
import com.gdsc.projectmiobackend.dto.UserDto;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    @ManyToOne
    @JoinColumn(name = "parent_comment_id")
    @JsonIgnore
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", fetch = FetchType.EAGER)
    private List<Comment> childComments = new ArrayList<>();

    public CommentDto toDto(){

        UserDto userDto = user.toDto();
        List<CommentDto> child = new ArrayList<>();

        if(childComments != null && !childComments.isEmpty()) {
            child = childComments.stream().map(Comment::toDto).collect(Collectors.toList());
        }
        return CommentDto.builder()
                .commentId(commentId)
                .content(content)
                .createDate(createDate)
                .postId(post.getId())
                .user(userDto)
                .childComments(child)
                .build();
    }
}
