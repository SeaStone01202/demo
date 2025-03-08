package com.seastone.demojwtrsa.Service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

/**
 * ✅ Service quản lý Refresh Token trong Redis
 * - Lưu Refresh Token vào Redis với thời gian sống cố định
 * - Xác thực Refresh Token có hợp lệ hay không
 * - Xóa Refresh Token khỏi Redis khi user logout
 */
@Service
public class RefreshTokenService {

    private final RedisTemplate<String, String> redisTemplate;
    private static final long REFRESH_TOKEN_TTL = 7 * 24 * 60 * 60; // ⏳ TTL: 7 ngày

    /**
     * Inject RedisTemplate để thao tác với Redis
     *
     * @param redisTemplate Dùng để lưu và truy xuất Refresh Token
     */
    public RefreshTokenService(RedisTemplate<String, String> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * ✅ Tạo Refresh Token mới cho user
     * - Sinh một UUID ngẫu nhiên làm Refresh Token
     * - Lưu vào Redis với thời gian sống là 7 ngày
     *
     * @param username Tên user để liên kết với Refresh Token
     * @return Chuỗi Refresh Token đã tạo
     */
    public String createRefreshToken(String username) {
        String refreshToken = UUID.randomUUID().toString(); // 🔑 Sinh UUID ngẫu nhiên làm token
        redisTemplate.opsForValue().set(
                "refreshToken:" + refreshToken, // Key: refreshToken:<token>
                username, // Value: username
                REFRESH_TOKEN_TTL, TimeUnit.SECONDS // TTL: 7 ngày
        );
        return refreshToken;
    }

    /**
     * ✅ Kiểm tra Refresh Token có hợp lệ không
     * - Nếu token tồn tại trong Redis → Trả về username
     * - Nếu token không tồn tại → Trả về null (hết hạn hoặc không hợp lệ)
     *
     * @param refreshToken Refresh Token cần kiểm tra
     * @return Username nếu token hợp lệ, null nếu không hợp lệ
     */
    public String validateRefreshToken(String refreshToken) {
        return redisTemplate.opsForValue().get("refreshToken:" + refreshToken);
    }

    /**
     * ✅ Xóa Refresh Token khỏi Redis (Khi user logout)
     *
     * @param refreshToken Refresh Token cần xóa
     */
    public void deleteRefreshToken(String refreshToken) {
        redisTemplate.delete("refreshToken:" + refreshToken);
    }
}
