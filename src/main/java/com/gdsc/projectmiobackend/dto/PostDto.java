package com.gdsc.projectmiobackend.dto;


import com.gdsc.projectmiobackend.entity.Category;
import com.gdsc.projectmiobackend.entity.Post;
import com.gdsc.projectmiobackend.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class PostDto {
    private Long postId;
    private String title;
    private String content;
    private LocalDateTime createDate;
    private Category category;
    private UserEntity user;
    private Long viewCount;
    private String fileName;
    private String filePath;

    public PostDto(Post post){
        this.postId = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.createDate = post.getCreateDate();
        this.category = post.getCategory();
        this.viewCount = post.getViewCount();
        this.user = post.getUser();
        this.fileName = post.getFileName();
        this.filePath = post.getFilePath();
    }
}
