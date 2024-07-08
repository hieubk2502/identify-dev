package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.request.ApiResponse;
import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.time.LocalDate;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
public class UserControllerWithTestContainer {

    @Container
    static final MySQLContainer<?> MY_SQL_CONTAINER = new MySQLContainer<>("mysql:8.0.37-debian");

    // if not config properties then mysqlContainer will set default value
    @DynamicPropertySource
    static void configureDatasource(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.driverClassName", () -> "com.mysql.cj.jdbc.Driver");
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "update");
    }

    @Autowired
    MockMvc mockMvc;

    UserCreateRequest request;

    ObjectMapper mapper;

    @BeforeEach
    public void init() {

        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());

        LocalDate dob = LocalDate.of(2006, 7, 7);

        request = UserCreateRequest
                .builder()
                .username("user0123")
                .password("123456")
                .firstname("tran")
                .lastname("hieu")
                .dob(dob)
                .build();
    }

    @Test
    public void ut_create() throws Exception {
        // given
        String content = mapper.writeValueAsString(request);

        // then
        mockMvc
                .perform(MockMvcRequestBuilders
                        .post("/users")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(content));
    }
}
