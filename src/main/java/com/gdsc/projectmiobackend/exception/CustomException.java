package com.gdsc.projectmiobackend.exception;

import com.gdsc.projectmiobackend.common.ErrorCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class CustomException extends RuntimeException {
    private final ErrorCode errorCode;

    public CustomException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public HttpStatus getHttpStatus() {
        return errorCode.getStatus();
    }

    public String getCode() {
        return errorCode.getCode();
    }

    @Override
    public String getMessage() {
        return errorCode.getMessage();
    }

}
