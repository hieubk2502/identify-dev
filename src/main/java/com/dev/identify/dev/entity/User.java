package com.dev.identify.dev.entity;


import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
import java.util.Set;

@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    String id;

    @Column(unique = true, columnDefinition = "varchar(255) COLLATE utf8_general_ci")
    String username;

    String password;

    String firstname;

    String lastname;

    LocalDate dob;

    @ManyToMany
    Set<Roles> roles;
}
