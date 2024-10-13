package com.tun.casestudy1.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
public enum ErrorCode {
    NOT_EXISTED(1102, "error.notfound", HttpStatus.BAD_REQUEST),
    DUPLICATE_EMAIL(1103, "error.duplicate", HttpStatus.BAD_REQUEST),
    DUPLICATE_NAME(1104, "error.name.duplicate", HttpStatus.BAD_REQUEST),
    FOLDER_CREATION_FAILED(1105, "error.folder.creation", HttpStatus.INTERNAL_SERVER_ERROR);

    ErrorCode(int code, String message, HttpStatusCode statusCode) {
        this.code = code;
        this.message = message;
        this.statusCode = statusCode;
    }

    private final int code;
    private final String message;
    private HttpStatusCode statusCode;
}
