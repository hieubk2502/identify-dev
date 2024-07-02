package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.PermissionRequest;
import com.dev.identify.dev.dto.response.PermissionResponse;
import com.dev.identify.dev.entity.Permission;
import com.dev.identify.dev.mapper.PermissionMapper;
import com.dev.identify.dev.repository.PermissionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PermissionService {

    PermissionRepository permissionRepository;

    PermissionMapper permissionMapper;

    public PermissionResponse create(PermissionRequest request) {
        Permission permission = permissionMapper.toPermission(request);
        permissionRepository.save(permission);

        return permissionMapper.toPermissionResponse(permission);
    }

    public List<PermissionResponse> getAllPermission() {

        return permissionRepository.findAll()
                .stream()
                .map(permissionMapper::toPermissionResponse)
                .toList();
    }

    public void deletePermission(String permission) {
        permissionRepository.deleteById(permission);
    }

}
