package ru.uip.docgen.openapi.generator.model;

import io.swagger.v3.oas.models.media.Content;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@EqualsAndHashCode
@ToString
@Getter
@AllArgsConstructor
public class ResponseSpec {
    private final String httpCode;
    private final String description;
    private final Content content;

    public List<ResponseTypeSpec> responseTypes() {
        if (content == null) {
            return List.of();
        } else {
            return content.entrySet().stream()
                    .map(e ->
                        new ResponseTypeSpec(e.getKey(), e.getValue().getSchema()))
                    .collect(Collectors.toList());
        }
    }
}
