package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.common.PostType;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import com.gdsc.projectmiobackend.dto.PostDto;
import jakarta.annotation.Nullable;
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

    //탑승자 수
    private Integer numberOfPassengers;

    private Long viewCount;

    private Double latitude;

    private Double longitude;

    private Long bookMarkCount;

    private Long participantsCount;

    private String location;

    private Long cost;

    private String isDeleteYN;

    private String region3Depth;

    @Enumerated(EnumType.STRING)
    private PostType postType;

    //성별
    @Nullable
    private Boolean gender;

    //흡연여부
    @Nullable
    private Boolean verifySmoker;

    //등하교 선택
    @Nullable
    private Boolean verifyGoReturn;

    @ManyToOne
    @JoinColumn
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Comment> commentList;

    @ManyToOne
    @JoinColumn
    private UserEntity user;

    @OneToMany(mappedBy = "post", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
    private List<Participants> participants;

    public PostDto toDto() {

        // participants가 null이면 빈 리스트로 초기화
        List<ParticipateDto> participantsList = (participants != null)
                ? participants.stream()
                .map(Participants::toDto)
                .filter(participant -> participant.getIsDeleteYN().equals("N") &&
                        !participant.getUserId().equals(user.getId())
                )
                .toList()
                : new ArrayList<>(); // null이면 빈 리스트로 처리
        return PostDto.builder()
                .postId(id)
                .title(title)
                .content(content)
                .createDate(createDate)
                .targetDate(targetDate)
                .targetTime(targetTime)
                .category(category)
                .verifyGoReturn(verifyGoReturn)
                .verifySmoker(verifySmoker)
                .gender(gender)
                .numberOfPassengers(numberOfPassengers)
                .participantsCount(participantsCount)
                .participants(participantsList)
                .user(user)
                .viewCount(viewCount)
                .latitude(latitude)
                .longitude(longitude)
                .location(location)
                .cost(cost)
                .isDeleteYN(isDeleteYN)
                .postType(postType)
                .bookMarkCount(bookMarkCount)
                .region3Depth(region3Depth)

                .build();
    }
}
