package ru.uip.docgen.gradle;

import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.docgen.contract.gradle.SpecDocsAction;
import ru.uip.docgen.contract.parser.ContractDescription;
import ru.uip.docgen.contract.parser.ContractsParser;
import ru.uip.docgen.openapi.OpenApiParser;
import ru.uip.docgen.plugin.SpecPlugin;
import ru.uip.docgen.plugin.SpecPluginExtension;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.mockito.Mockito.*;
import static ru.uip.docgen.plugin.SpecPlugin.PLUGIN_ID;

@Tag("unit")
public class SpecDocsActionTest {

    private Project project;

    private final SpecDocsAction specDocsAction = new SpecDocsAction();

    @BeforeEach
    public void setUp() {
        this.project = ProjectBuilder.builder()
                .withProjectDir(new File("./"))
                .withGradleUserHomeDir(new File(System.getProperty("java.io.tmpdir")))
                .build();
        this.project.getPluginManager().apply(PLUGIN_ID);
    }

    @Test
    public void testFromConfig() {
        SpecPluginExtension extension = new SpecPluginExtension();
        extension.setApiSpec("./src/test/api/api.yaml");
        final ConfigurableFileCollection files = project.files("./src/test/contracts/getAccounts.yml");

        Map<String, ConfigurableFileCollection> contracts = new HashMap<>();
        contracts.put("GetAccounts", files);
        extension.setOperationContracts(contracts);

        final Map<String, Set<File>> contractConfig = specDocsAction.fromConfig(extension);
        final Set<String> fileNames = contractConfig.values().stream()
                .flatMap(Collection::stream)
                .map(File::getName)
                .collect(Collectors.toSet());

        assertThat(fileNames, containsInAnyOrder("getAccounts.yml"));
    }

    @Test
    public void testParseSpec() {
        final SpecPlugin specPlugin = new SpecPlugin();
        final OpenApiParser openApiParser = mock(OpenApiParser.class);
        final ContractsParser contractsParser = mock(ContractsParser.class);

        specDocsAction.parseSpec(openApiParser, contractsParser, project.getLogger());

        verify(openApiParser, times(1)).parseOperationIds();
        verify(contractsParser, times(1)).parse();
    }

    @Test
    public void testFindOperationsWithoutContracts() {
        List<String> operations = Arrays.asList("GetAccounts", "UpdateAccounts");
        Map<String, Set<ContractDescription>> specs = new HashMap<>();
        final Set<String> uncoveredOperation = specDocsAction.findOperationsWithoutContracts(operations, specs, project.getLogger());
        assertThat(uncoveredOperation, containsInAnyOrder("GetAccounts", "UpdateAccounts"));
    }

    @Test
    public void testFindContractsWithUnknownOperations() {
        List<String> operations = Arrays.asList("GetAccounts", "UpdateAccounts");
        Map<String, Set<ContractDescription>> specs = new HashMap<>();
        ContractDescription contractDescription = new ContractDescription("Unknown", "Unknown");
        Set<ContractDescription> contractDescriptionSet = new HashSet<>();
        contractDescriptionSet.add(contractDescription);
        specs.put("Unknown", contractDescriptionSet);

        final Set<String> unknownOps = specDocsAction.findContractsWithUnknownOperations(operations, specs, project.getLogger());
        assertThat(unknownOps, containsInAnyOrder("Unknown"));

    }
}
