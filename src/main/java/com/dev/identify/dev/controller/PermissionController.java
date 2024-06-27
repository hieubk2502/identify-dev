package com.dev.identify.dev.controller;

import com.dev.identify.dev.dto.request.ApiResponse;
import com.dev.identify.dev.dto.request.PermissionRequest;
import com.dev.identify.dev.dto.response.PermissionResponse;
import com.dev.identify.dev.service.PermissionService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequestMapping("/permissions")
@Slf4j
public class PermissionController {

    PermissionService permissionService;

    @PostMapping
    public ApiResponse<PermissionResponse> create(@RequestBody PermissionRequest request) {
        return ApiResponse
                .<PermissionResponse>builder()
                .body(permissionService.create(request))
                .build();
    }

    @GetMapping
    public ApiResponse<List<PermissionResponse>> getAll() {
        return ApiResponse
                .<List<PermissionResponse>>builder()
                .body(permissionService.getAllPermission())
                .build();
    }

    @DeleteMapping("/{permission}")
    public ApiResponse<Void> deletePermission(@PathVariable("permission") String permission) {
        permissionService.deletePermission(permission);
        return ApiResponse
                .<Void>builder()
                .build();
    }

}
