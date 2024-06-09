package com.dev.identify.dev.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public enum ErrorCode {
    UNCATEGORIZED_EXCEPTION(9999, "Not categorized exception"),
    INVALID_KEY(1000, "Invalid_key"),
    USER_EXISTED(1001, "User existed"),
    USERNAME_INVALID(1002, "Username must be a least at 4 characters"),
    PASSWORD_INVALID(1002, "Password must be a least at 4 characters")

    ;

    int code;
    String message;
}
