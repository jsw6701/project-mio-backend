package com.gdsc.projectmiobackend.dto;

import com.gdsc.projectmiobackend.common.ApprovalOrReject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParticipateDto {
    private Long participantId;
    private Long postId;
    private Long userId;
    private Long postUserId;
    private String content;
    private ApprovalOrReject approvalOrReject;
    private Boolean driverMannerFinish;
    private Boolean passengerMannerFinish;
    private Boolean verifyFinish;
    private String isDeleteYN;
    private Boolean verifySmoker;
    private Boolean gender;
    private Boolean verifyGoReturn;
}
