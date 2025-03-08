package com.seastone.demojwtrsa.Configuration;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@AllArgsConstructor
public class SecurityConfig {

    private final JwtDecoder jwtDecoder;

    /**
     * ✅ Cấu hình bảo mật cho ứng dụng Spring Security
     * - Ngăn chặn truy cập trái phép vào các API
     * - Bật xác thực bằng JWT cho các request
     * 🔹 Cấu hình cụ thể:
     * - Cho phép truy cập công khai vào các endpoint `/auth/login`, `/auth/refresh`, `/auth/logout`
     * - Mọi request khác đều yêu cầu xác thực
     * - Sử dụng OAuth2 Resource Server với JWT để xác thực người dùng
     * @param http Đối tượng cấu hình bảo mật của Spring Security
     * @return SecurityFilterChain - Chuỗi bộ lọc bảo mật đã được thiết lập
     * @throws Exception Nếu có lỗi trong quá trình cấu hình bảo mật
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // ❌ Tắt CSRF (Cross-Site Request Forgery) vì API không sử dụng session
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()   // ✅ Cho phép truy cập mà không cần đăng nhập
                        .requestMatchers("/auth/refresh").permitAll() // ✅ Cho phép lấy Access Token mới mà không cần login lại
                        .requestMatchers("/auth/logout").permitAll()  // ✅ Cho phép logout mà không cần xác thực trước
                        .anyRequest().authenticated() // 🚀 Các request khác đều yêu cầu xác thực bằng JWT
                )
                .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder))); // 🛡️ Sử dụng JWT để xác thực

        return http.build();
    }

}
