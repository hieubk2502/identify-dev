package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.AuthenticationRequest;
import com.dev.identify.dev.dto.request.IntrospectRequest;
import com.dev.identify.dev.dto.request.LogoutRequest;
import com.dev.identify.dev.dto.request.RefreshRequest;
import com.dev.identify.dev.dto.response.AuthenticationResponse;
import com.dev.identify.dev.dto.response.IntrospectResponse;
import com.dev.identify.dev.entity.InvalidatedToken;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
import com.dev.identify.dev.repository.InvalidatedTokenRepository;
import com.dev.identify.dev.repository.UserRepository;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.StringJoiner;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class AuthenticationService {

    UserRepository userRepository;

    InvalidatedTokenRepository invalidatedTokenRepository;

    PasswordEncoder passwordEncoder;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @NonFinal
    @Value("${jwt.valid-token-duration}")
    int VALID_TOKEN_DURATION;

    @NonFinal
    @Value("${jwt.refreshable-token-duration}")
    int REFRESHABLE_TOKEN_DURATION;

    public IntrospectResponse introspect(IntrospectRequest request) throws Exception {

        String token = request.getToken();
        boolean isValid = true;
        try {
            verifyToken(token, false);
        } catch (Exception ex) {
            isValid = false;
        }

        return IntrospectResponse
                .builder()
                .valid(isValid)
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse
                .builder()
                .authenticate(true)
                .token(generateToken(user))
                .build();

    }

    public void logout(LogoutRequest request) throws Exception {

        // because when logout, use token not expired or expired, must check follow time of refresh token
        SignedJWT signedToken = verifyToken(request.getToken(), true);

        String jit = signedToken.getJWTClaimsSet().getJWTID();

        Date expiredTime = signedToken.getJWTClaimsSet().getExpirationTime();

        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(jit)
                .expiredTime(expiredTime)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);
    }

    public SignedJWT verifyToken(String token, boolean isRefresh) throws Exception {

        JWSVerifier verifier = new MACVerifier(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));

        SignedJWT signedJWT = SignedJWT.parse(token);

        Date expired = (isRefresh) ?
                new Date(signedJWT.getJWTClaimsSet().getIssueTime().toInstant().plus(REFRESHABLE_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli())
                : signedJWT.getJWTClaimsSet().getExpirationTime();

        // pre verify token , r toi check xem token co ton tai hay khong

        boolean verified = signedJWT.verify(verifier);

        if (!verified && expired.after(new Date())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }


        if (invalidatedTokenRepository.existsById(signedJWT.getJWTClaimsSet().getJWTID())) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }

        return signedJWT;

    }

    public AuthenticationResponse refreshToken(RefreshRequest request) throws Exception {
        // kiem tra xem token con hieu luc khong

        SignedJWT signedJWT = verifyToken(request.getToken(), true);

        // neu con hieu luu lay uuid ra
        String uuid = signedJWT.getJWTClaimsSet().getJWTID();
        Date expiredDate = signedJWT.getJWTClaimsSet().getExpirationTime();

        // tao doi luong luu trong db
        InvalidatedToken invalidatedToken = InvalidatedToken
                .builder()
                .id(uuid)
                .expiredTime(expiredDate)
                .build();

        invalidatedTokenRepository.save(invalidatedToken);

        // gen new token

        String username = signedJWT.getJWTClaimsSet().getSubject();
        User user = userRepository.findByUsername(username).orElseThrow(
                () -> new AppException(ErrorCode.UNAUTHENTICATED)
        );

        String generateToken = generateToken(user);

        return AuthenticationResponse
                .builder()
                .token(generateToken)
                .authenticate(true)
                .build();
    }

    // su dung nimbus jose de generate token
    private String generateToken(User user) {

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(user.getUsername())
                .issuer("dev.com") // token duoc issuer tu ai, thuong la domain project
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(VALID_TOKEN_DURATION, ChronoUnit.SECONDS).toEpochMilli()))
                // defind 1 id for token, save token if it logout
                .jwtID(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .build();

        Payload payload = new Payload(jwtClaimsSet.toJSONObject());

        JWSObject jwsObject = new JWSObject(header, payload);

        try {
            jwsObject.sign(new MACSigner(SIGNER_KEY.getBytes(StandardCharsets.UTF_8)));
            return jwsObject.serialize();
        } catch (JOSEException e) {
            log.error("Can't create token. ");
            throw new RuntimeException(e);
        }
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");

        // need to distinguish between ROLE and PERMISSION
        if (!CollectionUtils.isEmpty(user.getRoles())) {
            user.getRoles().forEach(roles -> {
                stringJoiner.add("ROLE_" + roles.getName());

                if (!CollectionUtils.isEmpty(roles.getPermissions())) {
                    roles.getPermissions().forEach(permission -> stringJoiner.add(permission.getName()));
                }
            });
        }
        return stringJoiner.toString();
    }
}
