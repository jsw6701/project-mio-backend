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

    // 로그인 에러
    INVALID_TOKEN_VALUE(HttpStatus.BAD_REQUEST, "AUTH_000", "올바르지 않은 토큰입니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "AUTH_001", "인증되지 않은 사용자입니다."),
    SUSPEND_USER(HttpStatus.FORBIDDEN, "AUTH_002", "탈퇴한 사용자입니다."),
    BLACKLIST_USER(HttpStatus.FORBIDDEN, "AUTH_003", "블랙리스트 사용자입니다."),

    // 유저 에러
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "USER_001", "존재하지 않는 유저입니다."),

    // 게시글 에러
    POST_NOT_FOUND(HttpStatus.NOT_FOUND, "POST_001", "존재하지 않는 게시글입니다."),
    POST_DEADLINE(HttpStatus.BAD_REQUEST, "POST_002", "마감된 게시글입니다."),
    POST_NOT_DELETE_TODAY(HttpStatus.BAD_REQUEST, "POST_003", "당일은 삭제할 수 없습니다."),
    POST_NOT_COMPLETED(HttpStatus.BAD_REQUEST, "POST_004", "완료되지 않은 게시글입니다."),
    POST_NOT_EMPTY_LOCATION(HttpStatus.BAD_REQUEST, "POST_005", "위치를 입력해주세요."),

    // 참여 에러
    PARTICIPATION_NOT_FOUND(HttpStatus.NOT_FOUND, "PARTICIPATION_001", "존재하지 않는 참여입니다."),
    PARTICIPATION_THIS_POST_DRIVER(HttpStatus.BAD_REQUEST, "PARTICIPATION_002", "이 게시글의 운전자는 참여할 수 없습니다."),
    PARTICIPATION_NOT_DELETE_TODAY(HttpStatus.BAD_REQUEST, "PARTICIPATION_003", "당일은 삭제할 수 없습니다."),
    PARTICIPATION_NOT_APPROVAL(HttpStatus.BAD_REQUEST, "PARTICIPATION_004", "승인되지 않은 참여입니다."),
    PARTICIPATION_NOT_REJECT(HttpStatus.BAD_REQUEST, "PARTICIPATION_005", "거절된 참여입니다."),
    PARTICIPATION_NOT_APPROVAL_OR_REJECT(HttpStatus.BAD_REQUEST, "PARTICIPATION_006", "승인 또는 거절되지 않은 참여입니다."),
    PARTICIPATION_FALSE_MANNER_STATUS(HttpStatus.BAD_REQUEST, "PARTICIPATION_007", "올바르지 않은 매너 상태입니다."),
    PARTICIPATION_ALREADY_REVIEW_DRIVER(HttpStatus.BAD_REQUEST, "PARTICIPATION_008", "이미 리뷰를 작성한 운전자입니다."),
    PARTICIPATION_ALREADY_REVIEW_PARTICIPANT(HttpStatus.BAD_REQUEST, "PARTICIPATION_009", "이미 리뷰를 작성한 참여자입니다."),
    PARTICIPATION_ALREADY_REVIEW(HttpStatus.BAD_REQUEST, "PARTICIPATION_010", "이미 평가한 유저입니다."),
    PARTICIPATION_NOT_REVIEW_SELF(HttpStatus.BAD_REQUEST, "PARTICIPATION_011", "자신에게 리뷰를 작성할 수 없습니다."),

    // 카테고리 에러
    CATEGORY_NOT_FOUND(HttpStatus.NOT_FOUND, "CATEGORY_001", "존재하지 않는 카테고리입니다."),

    // 댓글 에러
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "COMMENT_001", "존재하지 않는 댓글입니다."),

    // 권한 에러
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "ACCESS_001", "접근 권한이 없습니다."),

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
