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
    private String content;
    private ApprovalOrReject approvalOrReject;

    public ParticipateDto(Long postId, Long userId, String content, ApprovalOrReject approvalOrReject) {
        this.postId = postId;
        this.userId = userId;
        this.content = content;
        this.approvalOrReject = approvalOrReject;
    }
}
