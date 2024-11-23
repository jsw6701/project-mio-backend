package com.gdsc.projectmiobackend.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
@Getter
public enum ErrorCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "ENTITY_001", "존재하지 않는 엔티티입니다."),

    INVALID_INPUT_VALUE(HttpStatus.BAD_REQUEST, "E1", "올바르지 않은 입력값입니다."),
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "E2", "잘못된 HTTP 메서드를 호출했습니다."),
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "E3", "서버 에러가 발생했습니다."),

    INVALID_TOKEN_VALUE(HttpStatus.BAD_REQUEST, "AUTH_000", "올바르지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증되지 않은 사용자입니다."),
    SUSPEND_USER(HttpStatus.FORBIDDEN, "AUTH_002", "탈퇴한 사용자입니다."),
    BLACKLIST_USER(HttpStatus.FORBIDDEN, "AUTH_003", "블랙리스트 사용자입니다.")
    ;



    private final String message;
    private final String code;
    private final HttpStatus status;

    ErrorCode(final HttpStatus status, final String code, final String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
