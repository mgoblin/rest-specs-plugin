package ru.uip.docgen.openapi.generator.spi;

import io.swagger.v3.oas.models.OpenAPI;

public interface APISpecGenerator {
    String generateSpec(OpenAPI openAPI);
}
