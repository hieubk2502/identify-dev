package com.dev.identify.dev.configuration;

import com.dev.identify.dev.entity.Roles;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
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

    // first run will error, because role ADMIN not reference to permission table
    // please comment .roles(roleAdmin)
    // later add permission table, add role and remove admin && create user admin

    @ConditionalOnProperty(value = "spring.datasource.url", havingValue = "jdbc:mysql://localhost:3306/identify")
    @Bean
    ApplicationRunner applicationRunner(UserRepository userRepository) {
        log.info("Init Application config .............");
        return args -> {

            boolean userAdminExist = userRepository.existsByUsername("admin");

            if (!userAdminExist) {

                Set<Roles> roleAdmin = new HashSet<>();
                Roles role = Roles.builder()
                        .name("ROLE_ADMIN")
                        .build();
                roleAdmin.add(role);

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
