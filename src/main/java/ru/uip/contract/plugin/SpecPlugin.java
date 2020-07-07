package ru.uip.contract.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.logging.Logger;
import org.gradle.api.logging.Logging;
import ru.uip.contract.parser.ContractDescription;
import ru.uip.contract.parser.ContractsParser;
import ru.uip.openapi.OpenApiParser;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class SpecPlugin implements Plugin<Project> {

    private final static Logger logger = Logging.getLogger(SpecPlugin.class);

    public final static String PLUGIN_ID = "ru.uip.contract.specs";
    public final static String TASK_ID = "generate-api-spec";
    public final static String EXTENSION_ID = "apiContractSpec";

    @Override
    public void apply(Project project) {
        final SpecPluginExtension apiExt = project.getExtensions().create(EXTENSION_ID, SpecPluginExtension.class);

        project.task(TASK_ID).doLast(task -> {
            final OpenApiParser openApiParser = new OpenApiParser(apiExt.getApiSpec());
            final ContractsParser contractsParser = new ContractsParser(fromConfig(apiExt));

            final Map<String, Set<ContractDescription>> spec = parseSpec(openApiParser, contractsParser);
        });

    }

    public Map<String, Set<File>> fromConfig(SpecPluginExtension apiExt) {
        Map<String, Set<File>> contractFiles = new HashMap<>();
        for(Map.Entry<String, ConfigurableFileCollection> entry: apiExt.getOperationContracts().entrySet()) {
            final ConfigurableFileCollection fileCollection = entry.getValue();
            final Set<File> files = fileCollection.getFiles();
            contractFiles.put(entry.getKey(), files);
        }
        return contractFiles;
    }

    public Map<String, Set<ContractDescription>> parseSpec(OpenApiParser openApiParser, ContractsParser contractsParser) {
        final List<String> operationIds = openApiParser.parseOperationIds();
        final Map<String, Set<ContractDescription>> spec = contractsParser.parse();

        // Validate all operation covered by contract docs
        operationIds.forEach(operationId -> {
            if (!spec.containsKey(operationId)) {
                logger.warn(String.format("Warn: Operation %s does not have contract docs", operationId));
            }
        });
        return spec;
    }
}
