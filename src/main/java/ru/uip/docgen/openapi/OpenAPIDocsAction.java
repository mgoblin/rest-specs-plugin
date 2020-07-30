package ru.uip.docgen.openapi;

import io.swagger.v3.oas.models.OpenAPI;
import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Task;
import org.gradle.api.logging.Logger;
import ru.uip.docgen.openapi.generator.spi.APISpecGenerator;
import ru.uip.docgen.openapi.parser.OpenApiParser;
import ru.uip.docgen.plugin.SpecPluginExtension;

import java.util.Objects;
import java.util.ServiceLoader;

@NonNullApi
public class OpenAPIDocsAction implements Action<Task> {

    public static final String TASK_ID = "contract-gen-spec";
    public static final String TASK_GROUP_ID = "documentation";
    public static final String TASK_DESCR = "Generate REST service specification from open api spec and contract tests";

    @Override
    public void execute(Task task) {
        final Logger logger = task.getLogger();

        final SpecPluginExtension apiExt = task.getProject().getExtensions().findByType(SpecPluginExtension.class);
        final String apiSpec = Objects.requireNonNull(apiExt).getApiSpec();

        final OpenApiParser openApiParser = new OpenApiParser(apiSpec);
        final OpenAPI openAPI = openApiParser.getOpenAPI();

        logger.info("Parse OpenAPI spec from {} to {}", apiSpec, openAPI);

        final APISpecGenerator apiSpecGenerator = ServiceLoader.load(APISpecGenerator.class)
                .stream()
                .filter(it -> it.type().getName().equals(apiExt.getGeneratorClass()))
                .findFirst().orElseThrow().get();

        logger.info("Find spec generator {}", apiSpecGenerator.getClass().getName());
    }

}
