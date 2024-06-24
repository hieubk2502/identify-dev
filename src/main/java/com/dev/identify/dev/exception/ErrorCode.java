package com.dev.identify.dev.exception;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Not categorized exception"),
    INVALID_KEY(1000, "Invalid_key"),
    USER_EXISTED(1001, "User existed"),
    USER_NOT_EXISTED(1002, "User not existed"),
    USERNAME_INVALID(1003, "Username must be a least at 4 characters"),
    PASSWORD_INVALID(1003, "Password must be a least at 4 characters"),
    UNAUTHENTICATED(1004, "Unauthenticated");

    int code;
    String message;
}
