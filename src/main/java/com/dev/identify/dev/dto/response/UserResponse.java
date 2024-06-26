package com.dev.identify.dev.dto.response;

import com.dev.identify.dev.entity.Roles;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    String id;

    String username;

    String password;

    String firstname;

    String lastname;

    LocalDate dob;

    Set<Roles> roles;
}
