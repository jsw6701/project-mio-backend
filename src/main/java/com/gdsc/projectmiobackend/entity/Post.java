package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.dto.PostDto;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@Setter
@Builder(toBuilder = true)
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@AllArgsConstructor
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
    private LocalDate targetDate;

    private LocalTime targetTime;

    //등하교 선택
    private Boolean verifyGoReturn;

    //탑승자 수
    private Integer numberOfPassengers;

    private Long viewCount;

    private Boolean verifyFinish;

    private Double latitude;

    private Double longitude;

    private Long bookMarkCount;

    private Long participantsCount;

    private String location;

    private Long cost;

    @ManyToOne
    @JoinColumn
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Comment> commentList = new ArrayList<>();

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE)
    private List<Participants> participants = new ArrayList<>();

    public PostDto toDto() {
        return PostDto.builder()
                .postId(id)
                .title(title)
                .content(content)
                .createDate(LocalDateTime.now())
                .targetDate(targetDate)
                .targetTime(targetTime)
                .category(category)
                .verifyGoReturn(verifyGoReturn)
                .numberOfPassengers(numberOfPassengers)
                .participantsCount(participantsCount)
                .user(user)
                .viewCount(viewCount)
                .verifyFinish(verifyFinish)
                .latitude(latitude)
                .longitude(longitude)
                .location(location)
                .cost(cost)
                .build();
    }
}
