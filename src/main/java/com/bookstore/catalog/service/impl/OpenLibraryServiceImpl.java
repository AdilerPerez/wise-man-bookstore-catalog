package com.bookstore.catalog.service.impl;

import com.bookstore.catalog.service.OpenLibraryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Service
public class OpenLibraryServiceImpl implements OpenLibraryService {

    private final HttpClient httpClient;

    @Autowired
    public OpenLibraryServiceImpl(HttpClient httpClient) {
        this.httpClient = httpClient;
    }

    @Override
    public String searchByQuery(String query) {
        HttpRequest request = getHttpRequest(query);
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (Exception e) {
            throw new RuntimeException("Error fetching data from OpenLibrary", e);
        }
    }

    private static HttpRequest getHttpRequest(String query) {
        String encodedQuery = URLEncoder.encode(query, StandardCharsets.UTF_8);
        String fields = "key,title,author_name,first_publish_year,isbn";
        String url = "https://openlibrary.org/search.json?q=" + encodedQuery + "&fields=" + fields + "&limit=20";
        String userAgent = "O Catalogo do Sabio/1.0 adlerperezbarros@hotmail.com";

        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("User-Agent", userAgent)
                .timeout(Duration.ofSeconds(10))
                .GET()
                .build();
    }
}