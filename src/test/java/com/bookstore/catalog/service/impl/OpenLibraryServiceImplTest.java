package com.bookstore.catalog.service.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OpenLibraryServiceImplTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private HttpResponse<String> httpResponse;

    @InjectMocks
    private OpenLibraryServiceImpl openLibraryService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should return response body when searchByQuery is successful")
    void testSearchByQuerySuccess() throws Exception {
        String query = "testQuery";
        String expectedResponse = "{\"docs\":[]}";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class))).thenReturn(httpResponse);
        when(httpResponse.body()).thenReturn(expectedResponse);

        String actualResponse = openLibraryService.searchByQuery(query);

        assertEquals(expectedResponse, actualResponse);
    }

    @Test
    @DisplayName("Should throw RuntimeException when searchByQuery fails")
    void testSearchByQueryFailure() throws Exception {
        String query = "testQuery";

        when(httpClient.send(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
            .thenThrow(new RuntimeException("Error fetching data from OpenLibrary"));

        RuntimeException exception = assertThrows(RuntimeException.class, () -> openLibraryService.searchByQuery(query));

        assertEquals("Error fetching data from OpenLibrary", exception.getMessage());
    }
}
