package com.bookstore.catalog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.net.http.HttpClient;
import java.time.Duration;

@Configuration
public class HttpClientConfig {
    @Bean
    public HttpClient openLibraryHttpClient() {
        return HttpClient.newBuilder()
               .version(HttpClient.Version.HTTP_2)
               .connectTimeout(Duration.ofSeconds(5))
               .build();
    }
}