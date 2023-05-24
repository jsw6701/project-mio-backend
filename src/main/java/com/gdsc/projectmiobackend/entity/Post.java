package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.PostDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    // 게시글 아이디
    private Long id;

    // 게시글 제목
    private String title;

    // 게시글 내용
    @Column(length = 2000)
    private String content;

    // 작성일
    private LocalDateTime createDate;

    private Long viewCount;

    private String fileName;

    private String filePath;

    @ManyToOne
    @JoinColumn
    // 카테고리 아이디
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private UserEntity user;



    @Builder
    public Post(Long postId, String title, String content, LocalDateTime createDate, Category category, UserEntity user, Long viewCount, String fileName, String filePath){
        this.id = postId;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.category = category;
        this.user = user;
        this.viewCount = viewCount;
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public PostDto toDto() {
        return PostDto.builder()
                .postId(id)
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .category(category)
                .user(user)
                .fileName(fileName)
                .filePath(filePath)
                .viewCount(viewCount)
                .build();
    }
}
