package com.dev.identify.dev.controller;


import com.dev.identify.dev.dto.request.ApiResponse;
import com.dev.identify.dev.dto.request.AuthenticationRequest;
import com.dev.identify.dev.dto.request.IntrospectRequest;
import com.dev.identify.dev.dto.request.LogoutRequest;
import com.dev.identify.dev.dto.response.AuthenticationResponse;
import com.dev.identify.dev.dto.response.IntrospectResponse;
import com.dev.identify.dev.service.AuthenticationService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationController {

    AuthenticationService authenticationService;

    @PostMapping("/token")
    public ApiResponse<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {

        return ApiResponse.<AuthenticationResponse>builder()
                .body(authenticationService.authenticate(request))
                .build();
    }

    @PostMapping("/introspect")
    public ApiResponse<IntrospectResponse> introspect(@RequestBody IntrospectRequest request) throws Exception {

        return ApiResponse.<IntrospectResponse>builder()
                .body(authenticationService.introspect(request))
                .build();
    }

    @PostMapping("/logout")
    public ApiResponse<Void> logout(@RequestBody LogoutRequest request) throws Exception {
        authenticationService.logout(request);

        return ApiResponse.<Void>builder()
                .message("Logout done!")
                .build();
    }

}
