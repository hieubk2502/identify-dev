package com.dev.identify.dev.service;


import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@SpringBootTest
@Slf4j
@ExtendWith(MockitoExtension.class)
@TestPropertySource("/application-test.properties")
public class UserServiceTest {

    @MockBean
    UserRepository userRepository;

    @Autowired
    UserService userService;

    UserCreateRequest request;

    User user;

    ObjectMapper mapper;

    @BeforeEach
    public void init() {

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        LocalDate dob = LocalDate.of(2006, 7, 7);

        request = UserCreateRequest
                .builder()
                .username("user01")
                .password("123456")
                .firstname("tran")
                .lastname("hieu")
                .dob(dob)
                .build();

        user = User
                .builder()
                .id("id01")
                .username("user01")
                .password("123456")
                .firstname("tran")
                .lastname("hieu")
                .dob(dob)
                .build();
    }

    @Test
    public void ut_createUser() {
        // given

        // when
        when(userRepository.existsByUsername(anyString())).thenReturn(false);

        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponse userResponse = userService.createUser(request);

        // then

        assertEquals("id01", userResponse.getId());
        assertEquals("user01", userResponse.getUsername());
    }
}
