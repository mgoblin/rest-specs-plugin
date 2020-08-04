package ru.uip.docgen.openapi.generator.model;

import io.swagger.v3.oas.models.media.ArraySchema;
import io.swagger.v3.oas.models.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseTypeSpec {
    private final String contentType;
    private final Schema<?> schema;

    public String schemaDescription() {
        if (schema instanceof ArraySchema) {
            return "array[" + ((ArraySchema) schema).getItems().get$ref() + "]";
        } else {
            return schema.get$ref();
        }
    }
}
