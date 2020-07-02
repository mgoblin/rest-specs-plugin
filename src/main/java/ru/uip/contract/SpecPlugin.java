package ru.uip.contract;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.parser.OpenAPIV3Parser;
import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class SpecPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final SpecPluginExtension ext = project.getExtensions().create("apiContractSpec", SpecPluginExtension.class);
        project.task("generate-api-spec").doLast(task -> {
            final OpenApiParser openApiParser = new OpenApiParser(ext.getApiSpec(), project);
            openApiParser.parseOperationIds().forEach(System.out::println);
        });
    }
}
