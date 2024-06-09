package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.request.ApiResponse;
import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.service.UserService;
import jakarta.validation.Valid;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/users")
public class UserController {

//    @RequiredArgsContructor required fields is final
    UserService userService;

    @PostMapping
    public ApiResponse<User> createUser(@RequestBody @Valid UserCreateRequest request) {

        return ApiResponse.<User>builder()
                .body(userService.createUser(request))
                .build();

    }

    @GetMapping
    List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{userId}")
    User getUser(@PathVariable("userId") String userId) {
        return userService.getUser(userId);
    }

    @DeleteMapping("/{userId}")
    String deleteUser(@PathVariable("userId") String userId) {
        userService.getUser(userId);
        return "Delete complete!";
    }

    @PutMapping("/{userId}")
    User updateUser(@PathVariable("userId") String userId, @RequestBody @Valid UserUpdateRequest request) {
        return userService.updateUsers(userId, request);
    }
}
