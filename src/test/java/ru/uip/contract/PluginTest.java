package ru.uip.contract;

import org.gradle.api.DefaultTask;
import org.gradle.api.Project;
import org.gradle.testfixtures.ProjectBuilder;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;

public class PluginTest {
    @Test
    public void testSpecsPluginTaskExists() {
        Project project = ProjectBuilder.builder().build();
        project.getPluginManager().apply("ru.uip.contract.specs");
        assertThat(project.getTasks().getByName("generate-api-spec"), instanceOf(DefaultTask.class));
    }
}
