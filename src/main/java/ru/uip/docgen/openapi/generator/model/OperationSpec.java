package ru.uip.docgen.openapi.generator.model;

import io.swagger.v3.oas.models.Operation;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;

@Getter
@EqualsAndHashCode
@AllArgsConstructor
public class OperationSpec {
    private final Operation operation;
    private final String httpMethod;
    private final String path;

    public boolean hasParams() {
        return operation.getParameters() != null && operation.getParameters().size() > 0;
    }

    public boolean hasRequestBody() {
        return operation.getRequestBody() != null;
    }

}
