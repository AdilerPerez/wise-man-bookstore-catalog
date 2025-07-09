package com.bookstore.catalog.config;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.net.http.HttpClient;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class HttpClientConfigTest {

    @Autowired
    private HttpClient openLibraryHttpClient;

    @Test
    @DisplayName("Should create HttpClient with correct configuration")
    void testHttpClientConfiguration() {
        assertNotNull(openLibraryHttpClient, "HttpClient should not be null");

        // Version check
        assertEquals(HttpClient.Version.HTTP_2,
                openLibraryHttpClient.version(),
                "HTTP version should be HTTP/2");

        // Timeout check
        assertTrue(openLibraryHttpClient.connectTimeout().isPresent(),
                "Connect timeout should be set");
        assertEquals(Duration.ofSeconds(5),
                openLibraryHttpClient.connectTimeout().get(),
                "Connect timeout should be 5 seconds");
    }

    @Test
    @DisplayName("Should create separate instances of HttpClient")
    void testHttpClientInstances() {
        HttpClientConfig config = new HttpClientConfig();
        HttpClient client1 = config.openLibraryHttpClient();
        HttpClient client2 = config.openLibraryHttpClient();

        assertNotNull(client1, "First HttpClient instance should not be null");
        assertNotNull(client2, "Second HttpClient instance should not be null");
        assertNotSame(client1, client2, "Should create different instances");
    }
}
