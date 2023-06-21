package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum Manner {
    GOOD("좋음"),
    NORMAL("보통"),
    BAD("나쁨");

    private final String manner;
}
