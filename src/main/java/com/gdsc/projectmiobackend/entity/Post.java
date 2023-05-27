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

    // 카풀 날짜
    private LocalDateTime targetDate;

    //등하교 선택
    private Boolean verifyGoReturn;

    //탑승자 수
    private Integer numberOfPassengers;

    private Long viewCount;

    private String fileName;

    private String filePath;

    private Boolean verifyFinish;

    @ManyToOne
    @JoinColumn
    // 카테고리 아이디
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    @ManyToMany
    @JoinTable(name = "post_participants",
            joinColumns = @JoinColumn(name = "post_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<UserEntity> participants = new ArrayList<>();

    @Builder
    public Post(Long postId, String title, String content, LocalDateTime createDate, LocalDateTime targetDate, Category category, Boolean verifyGoReturn, Integer numberOfPassengers, UserEntity user, Long viewCount, String fileName, String filePath, Boolean verifyFinish){
        this.id = postId;
        this.title = title;
        this.content = content;
        this.createDate = createDate;
        this.targetDate = targetDate;
        this.category = category;
        this.verifyGoReturn = verifyGoReturn;
        this.numberOfPassengers = numberOfPassengers;
        this.user = user;
        this.viewCount = viewCount;
        this.fileName = fileName;
        this.filePath = filePath;
        this.verifyFinish = verifyFinish;
    }

    public PostDto toDto() {
        return PostDto.builder()
                .postId(id)
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .category(category)
                .verifyGoReturn(verifyGoReturn)
                .numberOfPassengers(numberOfPassengers)
                .user(user)
                .fileName(fileName)
                .filePath(filePath)
                .viewCount(viewCount)
                .verifyFinish(verifyFinish)
                .participants(participants)
                .build();
    }
}
