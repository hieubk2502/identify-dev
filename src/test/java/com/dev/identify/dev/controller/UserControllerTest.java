package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.dev.identify.dev.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDate;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@TestPropertySource("/application-test.properties")
public class UserControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    UserService userService;

    UserCreateRequest request;

    UserResponse response;

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

        response = UserResponse
                .builder()
                .username("user01")
                .firstname("tran")
                .lastname("hieu")
                .dob(dob)
                .build();
    }

    @Test
    public void ut_create() throws Exception {
        // given
        String content = mapper.writeValueAsString(request);

        // when
        when(userService.createUser(any(UserCreateRequest.class))).thenReturn(response);

        // then

        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content))
                .andExpect(
                        status().is(HttpStatus.OK.value())); // issue
    }

}
