package com.dev.identify.dev.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Not categorized exception", HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_KEY(1000, "Invalid_key", HttpStatus.BAD_REQUEST),
    USER_EXISTED(1001, "User existed", HttpStatus.BAD_REQUEST),
    USER_NOT_EXISTED(1002, "User not existed", HttpStatus.NOT_FOUND),
    USERNAME_INVALID(1003, "Username must be a least at {min} characters", HttpStatus.BAD_REQUEST),
    PASSWORD_INVALID(1004, "Password must be a least at {min} characters", HttpStatus.BAD_REQUEST),
    UNAUTHENTICATED(1005, "Unauthenticated", HttpStatus.UNAUTHORIZED),
    UNAUTHORIZED(1006, "Unauthorized", HttpStatus.FORBIDDEN),
    INVALID_DOB(1007, "Your age must be at least {min}", HttpStatus.BAD_REQUEST),
    EXPIRED_TOKEN(1008, "Token expired!", HttpStatus.UNAUTHORIZED);

    int code;
    String message;
    HttpStatusCode status;
}
