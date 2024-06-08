package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.UserCreateRequest;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.repository.UserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserService  {

    @Autowired
    UserRepository userRepository;

    public User createUser(UserCreateRequest request) {
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
        Optional<User> result =  userRepository.findById(userId);
        return result.get();
    }

    public void deleteUser(String userId) {
        User user =  userRepository.findById(userId).get();
        userRepository.delete(user);
    }

    public User updateUsers(String userId, UserCreateRequest request) {
        User user = userRepository.findById(userId).get();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());

        return userRepository.save(user);
    }
}
