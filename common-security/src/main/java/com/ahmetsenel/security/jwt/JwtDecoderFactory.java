package com.ahmetsenel.security.jwt;

import io.jsonwebtoken.io.Decoders;
import lombok.NoArgsConstructor;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

@NoArgsConstructor
public class JwtDecoderFactory {

    public static NimbusJwtDecoder create(String secretKey) {

        byte[] keyBytes =
                Decoders.BASE64.decode(secretKey);

        SecretKey key =
                new SecretKeySpec(keyBytes, "HmacSHA256");

        return NimbusJwtDecoder
                .withSecretKey(key)
                .build();
    }
}
