package ru.uip.docgen.openapi.generator.impl;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import ru.uip.docgen.openapi.generator.model.OperationSpec;
import ru.uip.docgen.openapi.generator.model.ResponseSpec;
import ru.uip.docgen.openapi.generator.spi.APISpecGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AsciidocGenerator implements APISpecGenerator {

    @Override
    public String generateSpec(OpenAPI openAPI, Map<String, Object> additionalAttributes) {
        try(StringWriter writer = new StringWriter();
            InputStream stream = this.getClass().getResourceAsStream("/index.mustache");
            InputStreamReader reader = new InputStreamReader(stream)) {

            final MustacheFactory mf = new DefaultMustacheFactory();
            final Mustache m = mf.compile(reader, "index.mustache");
            m.execute(writer, createContext(openAPI, additionalAttributes));

            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> createContext(OpenAPI openAPI, Map<String, Object> additionalAttributes) {
        Map<String, Object> context = new HashMap<>(additionalAttributes);
        context.put("appName", openAPI.getInfo().getTitle());
        context.put("description", openAPI.getInfo().getDescription());
        context.put("headerAttributes", headerAttributes(openAPI));
        context.put("apiInfo", apiInfo(openAPI));
        return context;
    }

    private Map<String, Object> apiInfo(OpenAPI openAPI) {
        Map<String, Object> apiInfo = new HashMap<>();
        List<OperationSpec> operations = new ArrayList<>();
        for(String path: openAPI.getPaths().keySet()) {
            generateOperationSpec(openAPI.getPaths().get(path).getGet(),path,  "GET", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getPut(),path,  "PUT", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getPost(),path,  "POST", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getDelete(),path,  "DELETE", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getOptions(),path,  "OPTIONS", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getHead(),path,  "HEAD", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getPatch(),path,  "PATCH", operations);
            generateOperationSpec(openAPI.getPaths().get(path).getTrace(),path,  "TRACE", operations);
        }
        apiInfo.put("operations", operations);
        return apiInfo;
    }

    public Map<String, String> headerAttributes(OpenAPI openAPI) {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("version", openAPI.getInfo().getVersion());
        attributes.put("appName", openAPI.getInfo().getTitle());
        return attributes;
    }

    public void generateOperationSpec(
            Operation operation,
            String path,
            String httpVerb,
            List<OperationSpec> operations) {

        if (operation != null) {
            final List<ResponseSpec> responses = operation.getResponses().entrySet().stream()
                    .map(e -> new ResponseSpec(
                                e.getKey(),
                                e.getValue().getDescription(),
                                e.getValue().getContent()))
                    .collect(Collectors.toList());
            final OperationSpec deleteSpec = new OperationSpec(
                    operation,
                    httpVerb,
                    path,
                    responses);
            operations.add(deleteSpec);
        }
    }
}
