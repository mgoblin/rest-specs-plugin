package ru.uip.docgen.openapi.generator.spi;

import io.swagger.v3.oas.models.OpenAPI;

import java.util.Map;

public interface APISpecGenerator {
    String generateSpec(OpenAPI openAPI, Map<String, Object> additionalAttributes);
}
