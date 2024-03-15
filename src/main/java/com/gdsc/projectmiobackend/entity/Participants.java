package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import com.gdsc.projectmiobackend.dto.ParticipateDto;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Participants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ApprovalOrReject approvalOrReject;

    @Nullable
    private Boolean verifyFinish;

    @Nullable
    private Boolean driverMannerFinish;

    @Nullable
    private Boolean passengerMannerFinish;

    @Nullable
    private String content;

    @Nullable
    private Long postUserId;

    @Builder
    public Participants(Post post, UserEntity user, String content, ApprovalOrReject approvalOrReject, Boolean verifyFinish, Boolean driverMannerFinish, Boolean passengerMannerFinish, Long postUserId) {
        this.post = post;
        this.user = user;
        this.content = content;
        this.approvalOrReject = approvalOrReject;
        this.verifyFinish = verifyFinish;
        this.driverMannerFinish = driverMannerFinish;
        this.passengerMannerFinish = passengerMannerFinish;
        this.postUserId = postUserId;
    }

    public ParticipateDto toDto() {
        return ParticipateDto.builder()
                .postId(post.getId())
                .userId(user.getId())
                .content(content)
                .build();
    }
}
