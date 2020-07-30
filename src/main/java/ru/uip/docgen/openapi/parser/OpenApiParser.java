package ru.uip.docgen.openapi.parser;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.parser.OpenAPIV3Parser;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Parse OpenAPI 3.x file
 *
 * Wrapper around io.swagger.v3.parser.OpenAPIV3Parser
 */
public class OpenApiParser {

    private final OpenAPI openAPI;

    public OpenApiParser(String fileName) {
        final File file = new File(fileName);
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("OpenApi spec file " + fileName + " not found");
        }
        this.openAPI =  new OpenAPIV3Parser().read(fileName);
    }

    public OpenAPI getOpenAPI() {
        return openAPI;
    }

    public List<String> parseOperationIds() {
        return openAPI.getPaths().values().stream()
                .flatMap(item -> item.readOperations().stream())
                .map(Operation::getOperationId)
                .collect(Collectors.toList());
    }
}
