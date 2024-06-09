package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class UserService  {

    UserRepository userRepository;

    public User createUser(UserCreateRequest request) {

        if(userRepository.existsByUsername(request.getUsername())){
            throw new AppException(ErrorCode.USER_EXISTED);
        }

        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setFirstname(request.getFirstname());
        user.setLastname(request.getLastname());
        user.setDob(request.getDob());
        return userRepository.save(user);
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(String userId) {
         return userRepository.findById(userId).orElseThrow(
                 () -> new RuntimeException("User not found")
         );
    }

    public void deleteUser(String userId) {
        User user =  userRepository.findById(userId).get();
        userRepository.delete(user);
    }

    public User updateUsers(String userId, UserUpdateRequest request) {
        User user = userRepository.findById(userId).get();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }
}
