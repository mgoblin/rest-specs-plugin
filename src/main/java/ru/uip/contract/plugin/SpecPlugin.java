package ru.uip.contract.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import ru.uip.contract.parser.ContractConfigParser;
import ru.uip.openapi.OpenApiParser;

public class SpecPlugin implements Plugin<Project> {

    public final static String PLUGIN_ID = "ru.uip.contract.specs";
    public final static String TASK_ID = "generate-api-spec";
    public final static String EXTENSION_ID = "apiContractSpec";

    @Override
    public void apply(Project project) {
        final SpecPluginExtension apiExt = project.getExtensions().create(EXTENSION_ID, SpecPluginExtension.class);

        project.task(TASK_ID).doLast(task -> {
            final OpenApiParser openApiParser = new OpenApiParser(apiExt.getApiSpec(), project);
            openApiParser.parseOperationIds().forEach(System.out::println);

            ContractConfigParser contractConfigParser = new ContractConfigParser(apiExt.getOperationContracts());
            contractConfigParser.parse();
        });

    }

}
