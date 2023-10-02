package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
public class ParticipateDto {
    private Long postId;
    private Long userId;
    private Long postUserId;
    private String content;
    private ApprovalOrReject approvalOrReject;

    public ParticipateDto(Long postId, Long userId, Long postUserId, String content, ApprovalOrReject approvalOrReject) {
        this.postId = postId;
        this.userId = userId;
        this.postUserId = postUserId;
        this.content = content;
        this.approvalOrReject = approvalOrReject;
    }
}
