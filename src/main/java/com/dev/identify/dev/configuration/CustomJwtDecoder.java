package com.dev.identify.dev.configuration;

import com.dev.identify.dev.dto.request.IntrospectRequest;
import com.dev.identify.dev.dto.response.IntrospectResponse;
import com.dev.identify.dev.exception.AppException;
import com.dev.identify.dev.exception.ErrorCode;
import com.dev.identify.dev.service.AuthenticationService;
import lombok.experimental.NonFinal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Component
public class CustomJwtDecoder implements JwtDecoder {

    @Autowired
    AuthenticationService authenticationService;

    private NimbusJwtDecoder nimbusJwtDecoder = null;

    @NonFinal
    @Value("${jwt.signerKey}")
    String SIGNER_KEY;

    @Override
    public Jwt decode(String token) {

        try {
            IntrospectResponse response = authenticationService.introspect(
                    IntrospectRequest
                            .builder()
                            .token(token)
                            .build());

            if (!response.isValid()) {
                throw new AppException(ErrorCode.EXPIRED_TOKEN);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (Objects.isNull(nimbusJwtDecoder)) {

            SecretKeySpec secretKeySpec = new SecretKeySpec(SIGNER_KEY.getBytes(StandardCharsets.UTF_8), MacAlgorithm.HS512.getName());

            nimbusJwtDecoder = NimbusJwtDecoder
                    .withSecretKey(secretKeySpec)
                    .macAlgorithm(MacAlgorithm.HS512)
                    .build();
        }

        return nimbusJwtDecoder.decode(token);
    }
}
