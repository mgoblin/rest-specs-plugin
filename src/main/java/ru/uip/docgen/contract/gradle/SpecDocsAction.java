package ru.uip.docgen.contract.gradle;

import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Task;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.api.logging.Logger;
import ru.uip.docgen.contract.generator.SpecGenerator;
import ru.uip.docgen.contract.generator.SpecWriter;
import ru.uip.docgen.contract.parser.ContractDescription;
import ru.uip.docgen.contract.parser.ContractsParser;
import ru.uip.docgen.openapi.OpenApiParser;
import ru.uip.docgen.plugin.SpecPluginExtension;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@NonNullApi
public class SpecDocsAction implements Action<Task> {

    public final static String TASK_ID = "generate-api-spec";
    public final static String TASK_GROUP_ID = "documentation";
    public final static String TASK_DESCR = "Generate REST service specification from open api spec and contract tests";

    @Override
    public void execute(Task task) {
        final Logger logger = task.getLogger();
        final SpecPluginExtension apiExt = task.getProject().getExtensions().findByType(SpecPluginExtension.class);
        final OpenApiParser openApiParser = new OpenApiParser(apiExt.getApiSpec());
        final ContractsParser contractsParser = new ContractsParser(fromConfig(apiExt));

        final SpecGenerator specGenerator = createSpecGenerator(apiExt);
        final SpecWriter specWriter = new SpecWriter(apiExt.getOutputDir());

        final Map<String, Set<ContractDescription>> operationContracts = parseSpec(openApiParser, contractsParser, logger);
        final Map<String, String> contractSpecs = specGenerator.generateSpecs(operationContracts);
        specWriter.write(contractSpecs);
    }

    private SpecGenerator createSpecGenerator(SpecPluginExtension apiExt) {
        return (apiExt.getTemplate() == null || apiExt.getTemplate().isBlank()) ?
                new SpecGenerator(apiExt.getSnippetsDir()) :
                new SpecGenerator(apiExt.getTemplate(), apiExt.getSnippetsDir());
    }

    public Map<String, Set<File>> fromConfig(SpecPluginExtension apiExt) {
        Map<String, Set<File>> contractFiles = new HashMap<>();
        for (Map.Entry<String, ConfigurableFileCollection> entry : apiExt.getOperationContracts().entrySet()) {
            final ConfigurableFileCollection fileCollection = entry.getValue();
            final Set<File> files = fileCollection.getFiles();
            contractFiles.put(entry.getKey(), files);
        }
        return contractFiles;
    }

    public Map<String, Set<ContractDescription>> parseSpec(
            OpenApiParser openApiParser,
            ContractsParser contractsParser,
            Logger logger) {
        final List<String> operationIds = openApiParser.parseOperationIds();
        final Map<String, Set<ContractDescription>> spec = contractsParser.parse();

        // Validate all operation covered by contract docs
        findOperationsWithoutContracts(operationIds, spec, logger);

        // Remove unknown spec docs
        final Set<String> contractsWithUnknownOperations = findContractsWithUnknownOperations(operationIds, spec, logger);
        contractsWithUnknownOperations.forEach(spec::remove);

        return spec;
    }

    public Set<String> findOperationsWithoutContracts(
            List<String> operationsId, Map<String,
            Set<ContractDescription>> specs,
            Logger logger) {
        final Set<String> specOperations = specs.keySet();
        final Set<String> undocumentedOperations = operationsId.stream()
                .filter(id -> !specOperations.contains(id))
                .collect(Collectors.toSet());
        undocumentedOperations.forEach(operationId ->
                logger.warn(String.format("Warn: Operation %s does not have contract docs", operationId)));
        return undocumentedOperations;
    }

    public Set<String> findContractsWithUnknownOperations(
            List<String> operationsId, Map<String,
            Set<ContractDescription>> specs,
            Logger logger) {
        final Set<String> specOperations = specs.keySet();
        final Set<String> contractsWithUnknownOperations = specOperations.stream()
                .filter(specOpId -> !operationsId.contains(specOpId))
                .collect(Collectors.toSet());
        contractsWithUnknownOperations.forEach(specOperationId ->
                logger.warn(String.format("Warn: Spec docs for %s does not match OpenAPI spc", specOperationId)));
        return contractsWithUnknownOperations;
    }

}
