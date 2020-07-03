package ru.uip.contract;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.api.Task;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.uip.contract.plugin.SpecPluginExtension;

import java.io.File;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.uip.contract.plugin.SpecPlugin.*;

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
    public void testExtensionConfigs() {
        final SpecPluginExtension extension = project.getExtensions().getByType(SpecPluginExtension.class);
        extension.setApiSpec("./src/test/api/api.yaml");
        final Task task = project.getTasks().getByName(TASK_ID);
        task.getActions().get(0).execute(task);
    }
}
