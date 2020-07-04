package ru.uip.contract;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.file.ConfigurableFileCollection;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.contract.parser.ContractConfigParser;
import ru.uip.contract.plugin.SpecPlugin;
import ru.uip.contract.plugin.SpecPluginExtension;
import ru.uip.openapi.OpenApiParser;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
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
        final ContractConfigParser contractConfigParser = mock(ContractConfigParser.class);

        specPlugin.parseSpec(openApiParser, contractConfigParser);

        verify(openApiParser, times(1)).parseOperationIds();
        verify(contractConfigParser, times(1)).parse();
    }
}
