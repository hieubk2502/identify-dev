package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.request.ApiResponse;
import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.dev.identify.dev.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
@Slf4j
public class UserController {

    //    @RequiredArgsContructor required fields is final
    UserService userService;

    @PostMapping
    public ApiResponse<UserResponse> createUser(@RequestBody @Valid UserCreateRequest request) {

        return ApiResponse.<UserResponse>builder()
                .body(userService.createUser(request))
                .build();

    }

    @GetMapping
    ApiResponse<List<UserResponse>> getUsers() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        log.info("Username: {}", authentication.getName());
        authentication.getAuthorities().forEach(grantedAuthority -> log.info(grantedAuthority.getAuthority()));

        return ApiResponse.<List<UserResponse>>builder()
                .body(userService.getUsers())
                .build();
    }

    @GetMapping("/{userId}")
    ApiResponse<UserResponse> getUser(@PathVariable("userId") String userId) {
        UserResponse userResponse = userService.getUser(userId);

        return ApiResponse.<UserResponse>builder()
                .body(userResponse)
                .build();
    }

    @DeleteMapping("/{userId}")
    ApiResponse<UserResponse> deleteUser(@PathVariable("userId") String userId) {
        UserResponse userResponse = userService.deleteUser(userId);

        return ApiResponse.<UserResponse>builder()
                .body(userResponse)
                .build();
    }

    @DeleteMapping
    ApiResponse<String> deleteAllUsers() {
        userService.deleteAllUsers();
        return ApiResponse.<String>builder()
                .body("Delete All success!")
                .build();
    }

    @PutMapping("/{userId}")
    ApiResponse<UserResponse> updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request) {
        UserResponse userResponse = userService.updateUsers(userId, request);

        return ApiResponse.<UserResponse>builder()
                .body(userResponse)
                .build();
    }
}
