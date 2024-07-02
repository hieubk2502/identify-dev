package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.dev.identify.dev.entity.Roles;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.enums.Role;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
import com.dev.identify.dev.mapper.UserMapper;
import com.dev.identify.dev.repository.RoleRepository;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class UserService {

    UserRepository userRepository;

    UserMapper userMapper;

    RoleRepository roleRepository;

    PasswordEncoder passwordEncoder;

    public UserResponse createUser(UserCreateRequest request) {

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = userMapper.toUser(request);
        // Bcrypt password
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        HashSet<Roles> roleSet = new HashSet<>();

        Roles roles = Roles.builder()
                .name(Role.USER.name())
                .build();
        roleSet.add(roles);
        user.setRoles(roleSet);

        return userMapper.toUserResponse(userRepository.save(user));
    }

    @PreAuthorize("hasRole('ADMIN')")
//    @PreAuthorize("hasAuthority('READ_DATA')")
    public List<UserResponse> getUsers() {
        log.info("In method get Users");
        return userMapper.toListUserResponse(userRepository.findAll());
    }

    @PostAuthorize("returnObject.username == authentication.name")
    public UserResponse getUser(String userId) {
        log.info("get user {}", SecurityContextHolder.getContext().getAuthentication().getName());
        return userMapper
                .toUserResponse(userRepository.findById(userId)
                        .orElseThrow(
                                () -> new RuntimeException("User not found"))
                );
    }

    public UserResponse getMyInfo() {
        String contextName = SecurityContextHolder.getContext().getAuthentication().getName();

        User user = userRepository.findByUsername(contextName)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        return userMapper.toUserResponse(user);

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
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        List<Roles> roles = roleRepository.findAllById(request.getRoles());

        user.setRoles(new HashSet<>(roles));

        return userMapper.toUserResponse(userRepository.save(user));
    }
}
