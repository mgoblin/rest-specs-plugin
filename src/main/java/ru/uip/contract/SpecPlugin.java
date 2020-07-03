package ru.uip.contract;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

public class SpecPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final SpecPluginExtension apiExt = project.getExtensions().create("apiContractSpec", SpecPluginExtension.class);

        project.task("generate-api-spec").doLast(task -> {
            final OpenApiParser openApiParser = new OpenApiParser(apiExt.getApiSpec(), project);
            openApiParser.parseOperationIds().forEach(System.out::println);

            ContractConfigParser contractConfigParser = new ContractConfigParser(apiExt.getOperationContracts());
            contractConfigParser.parse();
        });

    }

}
