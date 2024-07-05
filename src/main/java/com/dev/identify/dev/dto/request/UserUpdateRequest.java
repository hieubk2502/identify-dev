package com.dev.identify.dev.dto.request;

import com.dev.identify.dev.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.List;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class UserUpdateRequest {

    String username;

    @Size(min = 4, message = "Password must be 4 characters")
    String password;

    String firstname;

    String lastname;

    LocalDate dob;

    @DobConstraint(min = 18, message = "INVALID_DOB")
    List<String> roles;
}
