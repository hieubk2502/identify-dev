package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.UserCreateRequest;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @PostMapping
    public User createUser(@RequestBody UserCreateRequest request) {
        return userService.createUser(request);
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
    User getUser(@PathVariable("userId") String userId,@RequestBody UserCreateRequest request) {
        return userService.updateUsers(userId, request);
    }
}
