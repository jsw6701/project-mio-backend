package com.gdsc.projectmiobackend.entity;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
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

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @Nullable
    @Enumerated(EnumType.STRING)
    private ApprovalOrReject approvalOrReject;

    @Nullable
    private Boolean verifyFinish;

    @Nullable
    private String content;

    public Participants(Post post, UserEntity user, String content) {
        this.post = post;
        this.user = user;
        this.content = content;
    }
}
