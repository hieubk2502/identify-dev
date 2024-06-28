package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.RoleRequest;
import com.dev.identify.dev.dto.response.RoleResponse;
import com.dev.identify.dev.entity.Permission;
import com.dev.identify.dev.entity.Roles;
import com.dev.identify.dev.mapper.RoleMapper;
import com.dev.identify.dev.repository.PermissionRepository;
import com.dev.identify.dev.repository.RoleRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RoleService {

    RoleRepository roleRepository;

    PermissionRepository permissionRepository;

    RoleMapper roleMapper;

    public RoleResponse create(RoleRequest request) {
        Roles roles = roleMapper.toRole(request);

        List<Permission> permissionList = permissionRepository.findAllById(request.getPermissions());

        roles.setPermissions(new HashSet<>(permissionList));

        Roles rolesResponse = roleRepository.save(roles);

        return roleMapper.toRoleResponse(rolesResponse);
    }

    public List<RoleResponse> getAllRoles() {
        return roleRepository.findAll()
                .stream()
                .map(roleMapper::toRoleResponse)
                .toList();
    }

    public void deleteRole(String role) {
        roleRepository.deleteById(role);
    }
}
