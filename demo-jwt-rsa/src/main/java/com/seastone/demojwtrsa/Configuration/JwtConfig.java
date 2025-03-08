package com.seastone.demojwtrsa.Configuration;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPublicKey;
import java.util.UUID;

@Configuration
public class JwtConfig {

    /**
     * ✅ 1. Tạo một cặp khóa RSA (Public Key & Private Key)
     * - Sử dụng thuật toán RSA với độ dài 2048-bit
     * - Được dùng để ký và xác thực JWT
     */
    @Bean
    public KeyPair keyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * ✅ 2. Chuyển KeyPair thành đối tượng RSAKey
     * - RSAKey chứa cả Public Key & Private Key
     * - Được dùng làm nguồn cung cấp khóa cho JWT
     */
    @Bean
    public RSAKey rsaKey(KeyPair keyPair) {
        return new RSAKey.Builder((RSAPublicKey) keyPair.getPublic()) // Public Key
                .privateKey(keyPair.getPrivate()) // Private Key
                .keyID(UUID.randomUUID().toString()) // Tạo ID ngẫu nhiên cho Key
                .build();
    }

    /**
     * ✅ 3. Tạo JwtEncoder để ký JWT bằng Private Key
     * - Sử dụng RSA Private Key để ký JWT
     * - Trả về NimbusJwtEncoder
     */
    @Bean
    public JwtEncoder jwtEncoder(RSAKey rsaKey) {
        JWKSet jwkSet = new JWKSet(rsaKey); // Đưa RSAKey vào JWKSet
        JWKSource<SecurityContext> jwkSource = (jwkSelector, securityContext) -> jwkSelector.select(jwkSet);
        return new NimbusJwtEncoder(jwkSource);
    }

    /**
     * ✅ 4. Tạo JwtDecoder để kiểm tra JWT bằng Public Key
     * - Dùng Public Key để xác thực chữ ký JWT
     * - Trả về NimbusJwtDecoder
     */
    @Bean
    public JwtDecoder jwtDecoder(RSAKey rsaKey) throws JOSEException {
        return NimbusJwtDecoder.withPublicKey(rsaKey.toRSAPublicKey()).build();
    }
}


