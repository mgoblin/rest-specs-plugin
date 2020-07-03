package ru.uip.contract;

import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.uip.openapi.OpenApiParser;

import java.io.File;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;
import static ru.uip.contract.plugin.SpecPlugin.PLUGIN_ID;

public class OpenApiParserTest {

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
    public void testHappyPath() {
        OpenApiParser parser = new OpenApiParser("./src/test/api/api.yaml", project);
        assertThat(parser, notNullValue());

        final List<String> operations = parser.parseOperationIds();
        assertThat(operations, hasItems(
                "GetAccounts",
                "CreateOrUpdateAccount",
                "FindAccountById",
                "DeleteAccount"
        ));
    }

    @Test
    public void testNonExistingFile() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new OpenApiParser("./src/test/api/no.file", project)
        );
    }
}
