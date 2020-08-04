package ru.uip.docgen.openapi.generator.model;

import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.MediaType;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class OperationSpec {
    private final Operation operation;
    private final String httpMethod;
    private final String path;
    private List<ResponseSpec> responses;

    public boolean hasParams() {
        return operation.getParameters() != null && operation.getParameters().size() > 0;
    }

    public boolean hasRequestBody() {
        return operation.getRequestBody() != null;
    }

    public Set<Map.Entry<String, MediaType>> requestContent() {
        return operation.getRequestBody().getContent().entrySet();
    }

    public boolean hasResponse() {
        return operation.getResponses() != null && operation.getResponses().size() > 0;
    }

}
