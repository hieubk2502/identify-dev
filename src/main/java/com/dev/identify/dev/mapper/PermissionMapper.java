package com.dev.identify.dev.mapper;

import com.dev.identify.dev.dto.request.PermissionRequest;
import com.dev.identify.dev.dto.response.PermissionResponse;
import com.dev.identify.dev.entity.Permission;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    Permission toPermission(PermissionRequest request);

    PermissionResponse toPermissionResponse(Permission permission);

}
