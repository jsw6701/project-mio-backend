package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum ApprovalOrReject {
    WAITING("대기"),
    APPROVAL("승인"),
    REJECT("거절"),
    FINISH("완료");

    private final String approvalOrReject;
}