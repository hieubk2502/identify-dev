package com.dev.identify.dev.configuration;

import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.enums.Role;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;
import java.util.Set;

@Configuration
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class ApplicationInitConfig {

    PasswordEncoder passwordEncoder;

    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        return args -> {

            boolean userAdminExist = userRepository.existsByUsername("admin");

            if (!userAdminExist) {

                Set<String> roleAdmin = new HashSet<>();
                roleAdmin.add(Role.ADMIN.name());

                User userAdmin = User
                        .builder()
                        .username("admin")
                        .password(passwordEncoder.encode("admin"))
                        .roles(roleAdmin)
                        .build();
                userRepository.save(userAdmin);
                log.warn("Admin user has been created with username: admin . password: admin");
            }
        };
    }
}
