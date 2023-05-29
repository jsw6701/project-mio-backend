package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum RoleType {
    ADMIN("어드민"),
    MEMBER("유저");

    private final String value;
}
