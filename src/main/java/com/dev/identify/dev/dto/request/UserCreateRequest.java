package com.dev.identify.dev.dto.request;

import com.dev.identify.dev.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateRequest {

    @Size(min = 4, message = "USERNAME_INVALID")
    String username;

    @Size(min = 4, message = "PASSWORD_INVALID")
    String password;

    String firstname;

    String lastname;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    LocalDate dob;
}
