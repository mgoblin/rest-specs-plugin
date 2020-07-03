package ru.uip.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.gradle.api.Project;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;

public class OpenApiParser {

    private final OpenAPI openAPI;

    public OpenApiParser(String fileName, Project project) {
        final File file = project.file(fileName);
        if (!file.exists() || !file.canRead()) {
            throw new IllegalArgumentException("OpenApi spec file " + fileName + " not found");
        }
        this.openAPI =  new OpenAPIV3Parser().read(fileName);
    }

    public List<String> parseOperationIds() {
        return openAPI.getPaths().values().stream()
                .flatMap(item -> item.readOperations().stream())
                .map(Operation::getOperationId)
                .collect(Collectors.toList());
    }
}
