package com.dev.identify.dev.mapper;

import com.dev.identify.dev.dto.request.UserCreateRequest;
import com.dev.identify.dev.dto.request.UserUpdateRequest;
import com.dev.identify.dev.dto.response.UserResponse;
import com.dev.identify.dev.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserCreateRequest request);

    void updateUser(@MappingTarget User user, UserUpdateRequest request);

    UserResponse toUserResponse(User user);

    List<UserResponse> toListUserResponse(List<User> users);
}
