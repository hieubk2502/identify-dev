package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserResponse;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
import com.dev.identify.dev.mapper.UserMapper;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        userRepository.save(user);
        return userMapper.toUserResponse(user);
    }

    public List<UserResponse> getUsers() {
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    public UserResponse getUser(String userId) {
        return userMapper
                .toUserResponse(userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException("User not found"))
                );
    }

    public UserResponse deleteUser(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserId not found"));
        userRepository.delete(user);

        return userMapper.toUserResponse(user);
    }

    public void deleteAllUsers() {
        userRepository.deleteAll();
    }


    public UserResponse updateUsers(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("UserId not found"));

        userMapper.updateUser(user, request);
        userRepository.save(user);

        return userMapper.toUserResponse(user);
    }
}
