package ru.uip.contract;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.contract.parser.ContractDescription;
import ru.uip.contract.parser.ContractsParser;
import ru.uip.contract.plugin.SpecPlugin;
import ru.uip.contract.plugin.SpecPluginExtension;
import ru.uip.openapi.OpenApiParser;

import java.io.File;
import java.util.*;
import java.util.stream.Collectors;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static ru.uip.contract.plugin.SpecPlugin.PLUGIN_ID;
import static ru.uip.contract.plugin.SpecPlugin.TASK_ID;

@Tag("unit")
public class PluginTest {

    private Project project;

    @BeforeEach
    public void setUp() {
        this.project = ProjectBuilder.builder()
                .withProjectDir(new File("./"))
                .withGradleUserHomeDir(new File(System.getProperty("java.io.tmpdir")))
                .build();
        this.project.getPluginManager().apply(PLUGIN_ID);
    }

    @Test
    public void testTaskExists() {
        assertThat(project.getTasks().getByName(TASK_ID), instanceOf(DefaultTask.class));
    }

    @Test
    public void testExtensionExists() {
        final SpecPluginExtension extension = project.getExtensions().getByType(SpecPluginExtension.class);

        assertThat(extension, notNullValue());
        assertThat(extension.getApiSpec(), equalTo("src/main/api/api.yaml"));
        assertThat(extension.getOperationContracts(), anEmptyMap());
    }

    @Test
    public void testFromConfig() {
        final SpecPlugin specPlugin = new SpecPlugin();

        SpecPluginExtension extension = new SpecPluginExtension();
        extension.setApiSpec("./src/test/api/api.yaml");
        final ConfigurableFileCollection files = project.files("./src/test/contracts/getAccounts.yml");
        Map<String, ConfigurableFileCollection> contracts = new HashMap<>();
        contracts.put("GetAccounts", files);
        extension.setOperationContracts(contracts);

        final Map<String, Set<File>> contractConfig = specPlugin.fromConfig(extension);
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

        specPlugin.parseSpec(openApiParser, contractsParser);

        verify(openApiParser, times(1)).parseOperationIds();
        verify(contractsParser, times(1)).parse();
    }

    @Test
    public void testFindOperationsWithoutContracts() {
        final SpecPlugin specPlugin = new SpecPlugin();

        List<String> operations = Arrays.asList("GetAccounts", "UpdateAccounts");
        Map<String, Set<ContractDescription>> specs = new HashMap<>();
        final Set<String> uncoveredOperation = specPlugin.findOperationsWithoutContracts(operations, specs);
        assertThat(uncoveredOperation, containsInAnyOrder("GetAccounts", "UpdateAccounts"));
    }

    @Test
    public void testFindContractsWithUnknownOperations() {
        final SpecPlugin specPlugin = new SpecPlugin();

        List<String> operations = Arrays.asList("GetAccounts", "UpdateAccounts");
        Map<String, Set<ContractDescription>> specs = new HashMap<>();
        ContractDescription contractDescription = new ContractDescription("Unknown", "Unknown");
        Set<ContractDescription> contractDescriptionSet = new HashSet<>();
        contractDescriptionSet.add(contractDescription);
        specs.put("Unknown", contractDescriptionSet);

        final Set<String> unknownOps = specPlugin.findContractsWithUnknownOperations(operations, specs);
        assertThat(unknownOps, containsInAnyOrder("Unknown"));

    }
}
