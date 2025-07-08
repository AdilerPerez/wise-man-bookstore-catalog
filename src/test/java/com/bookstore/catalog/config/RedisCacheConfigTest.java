package com.bookstore.catalog.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(MockitoExtension.class)
public class RedisCacheConfigTest {

    private final RedisCacheConfig redisCacheConfig = new RedisCacheConfig();

    @Test
    @DisplayName("Should create RedisCacheConfiguration with correct settings")
    void testCacheConfiguration() {
        RedisCacheConfiguration cacheConfiguration = redisCacheConfig.cacheConfiguration();

        assertNotNull(cacheConfiguration, "Cache configuration should not be null");
        assertEquals(Duration.ofMinutes(1), cacheConfiguration.getTtl(), "TTL should be 1 minute");
        assertFalse(cacheConfiguration.getAllowCacheNullValues(), "Null values should be disabled");
    }
}
