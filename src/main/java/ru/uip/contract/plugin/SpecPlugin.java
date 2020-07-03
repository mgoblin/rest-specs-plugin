package ru.uip.contract.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import ru.uip.contract.parser.ContractConfigParser;
import ru.uip.openapi.OpenApiParser;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

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

            Map<String, Set<File>> contractFiles = new HashMap<>();
            for(Map.Entry<String, ConfigurableFileCollection> entry :apiExt.getOperationContracts().entrySet()) {
                final ConfigurableFileCollection fileCollection = entry.getValue();
                final Set<File> files = fileCollection.getFiles();
                contractFiles.put(entry.getKey(), files);
            }

            ContractConfigParser contractConfigParser = new ContractConfigParser(contractFiles);
            contractConfigParser.parse();
        });

    }

}
