package com.bookstore.catalog.config;

import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.stereotype.Component;

@Component
public class CustomHealthIndicator implements HealthIndicator {

    @Override
    public Health health() {
        try {
            return Health.up()
                    .withDetail("app", "Bookstore Catalog")
                    .withDetail("version", "1.0.0")
                    .withDetail("status", "Running")
                    .build();
        } catch (Exception e) {
            return Health.down()
                    .withDetail("error", e.getMessage())
                    .build();
        }
    }
}