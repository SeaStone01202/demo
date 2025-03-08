package com.seastone.demojwtrsa.Configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    /**
     * ✅ Cấu hình RedisTemplate để thao tác với Redis
     * - Sử dụng RedisTemplate để lưu và truy xuất dữ liệu từ Redis
     * - Key và Value sẽ được lưu dưới dạng String
     *
     * @param redisConnectionFactory Kết nối Redis được Spring Boot quản lý
     * @return RedisTemplate<String, String> để thao tác với Redis
     */
    @Bean
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory); // Sử dụng factory để kết nối Redis

        // Sử dụng StringRedisSerializer để đảm bảo dữ liệu được lưu dưới dạng String
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new StringRedisSerializer());

        return template;
    }
}
