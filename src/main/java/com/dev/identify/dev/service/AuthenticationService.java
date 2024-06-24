package com.dev.identify.dev.service;

import com.dev.identify.dev.dto.request.AuthenticationRequest;
import com.dev.identify.dev.dto.request.IntrospectRequest;
import com.dev.identify.dev.dto.response.AuthenticationResponse;
import com.dev.identify.dev.dto.response.IntrospectResponse;
import com.dev.identify.dev.entity.User;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AuthenticationService {

    private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);
    UserRepository userRepository;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    public IntrospectResponse introspect(IntrospectRequest request) throws Exception{
        String token = request.getToken();

        JWSVerifier jwsVerifier = new MACVerifier(SIGNER_KEY.getBytes(StandardCharsets.UTF_8));

        // parse token
        SignedJWT signedJWT = SignedJWT.parse(token);

        // check time expired
        Date expirationTime = signedJWT.getJWTClaimsSet().getExpirationTime();

        boolean verify = signedJWT.verify(jwsVerifier);

        return IntrospectResponse
                .builder()
                .valid(verify && expirationTime.after(new Date()))
                .build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));

        PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(10);

        boolean authenticate = passwordEncoder.matches(request.getPassword(), user.getPassword());

        if (!authenticate) {
            throw new AppException(ErrorCode.UNAUTHENTICATED);
        }
        return AuthenticationResponse
                .builder()
                .authenticate(true)
                .token(generateToken(request.getUsername()))
                .build();

    }

    // su dung nimbus jose de generate token
    private String generateToken(String username){

        JWSHeader header = new JWSHeader(JWSAlgorithm.HS512);

        JWTClaimsSet jwtClaimsSet = new JWTClaimsSet.Builder()
                .subject(username)
                .issuer("dev.com") // token duoc issuer tu ai, thuong la domain project
                .issueTime(new Date())
                .expirationTime(new Date(Instant.now().plus(1, ChronoUnit.DAYS).toEpochMilli()))
                .claim("userId", "Custom")
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
}
