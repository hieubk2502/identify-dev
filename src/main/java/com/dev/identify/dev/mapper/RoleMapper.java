package com.dev.identify.dev.mapper;

import com.dev.identify.dev.dto.request.RoleRequest;
import com.dev.identify.dev.dto.response.RoleResponse;
import com.dev.identify.dev.entity.Roles;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RoleMapper {

    @Mapping(target = "permissions", ignore = true)
    Roles toRole(RoleRequest request);

    RoleResponse toRoleResponse(Roles role);

}
