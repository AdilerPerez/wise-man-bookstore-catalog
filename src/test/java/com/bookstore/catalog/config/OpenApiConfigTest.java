package com.bookstore.catalog.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OpenApiConfigTest {

    @Test
    @DisplayName("Returns OpenAPI object with correct title, description, and version")
    void returnsOpenApiObjectWithCorrectInfo() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        Info info = openAPI.getInfo();
        Assertions.assertEquals("Wise Man Book Catalog", info.getTitle());
        Assertions.assertEquals("Book catalog management API", info.getDescription());
        Assertions.assertEquals("1.0.0", info.getVersion());
    }

    @Test
    @DisplayName("Includes correct contact information in OpenAPI object")
    void includesCorrectContactInformation() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        Assertions.assertNotNull(openAPI.getInfo().getContact());
        Assertions.assertEquals("Adler Perez", openAPI.getInfo().getContact().getName());
        Assertions.assertEquals("adlerperezbarros@hotmail.com", openAPI.getInfo().getContact().getEmail());
        Assertions.assertEquals("https://github.com/AdilerPerez", openAPI.getInfo().getContact().getUrl());
    }

    @Test
    @DisplayName("Adds security scheme with correct name and type")
    void addsSecuritySchemeWithCorrectNameAndType() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        SecurityScheme securityScheme = openAPI.getComponents().getSecuritySchemes().get("bearerAuth");
        Assertions.assertNotNull(securityScheme);
        Assertions.assertEquals(SecurityScheme.Type.HTTP, securityScheme.getType());
        Assertions.assertEquals("bearer", securityScheme.getScheme());
        Assertions.assertEquals("JWT", securityScheme.getBearerFormat());
    }

    @Test
    @DisplayName("Adds security requirement with correct scheme name")
    void addsSecurityRequirementWithCorrectSchemeName() {
        OpenApiConfig config = new OpenApiConfig();
        OpenAPI openAPI = config.customOpenAPI();

        SecurityRequirement securityRequirement = openAPI.getSecurity().get(0);
        Assertions.assertTrue(securityRequirement.containsKey("bearerAuth"));
    }
}