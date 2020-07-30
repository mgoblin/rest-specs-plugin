package ru.uip.docgen.plugin;

import org.gradle.api.Plugin;
import org.gradle.api.Project;
import org.gradle.api.Task;
import ru.uip.docgen.contract.gradle.SpecDocsAction;
import ru.uip.docgen.openapi.OpenAPIDocsAction;


public class SpecPlugin implements Plugin<Project> {

    public final static String PLUGIN_ID = "ru.uip.docgen.contract.specs";
    public final static String EXTENSION_ID = "apiContractSpec";

    private final OpenAPIDocsAction openAPIDocsAction = new OpenAPIDocsAction();
    private final SpecDocsAction specDocsAction = new SpecDocsAction();

    @Override
    public void apply(Project project) {
        project.getExtensions().create(EXTENSION_ID, SpecPluginExtension.class);

        final Task specDocsTask = project.task(SpecDocsAction.TASK_ID).doLast(specDocsAction);
        specDocsTask.setGroup(SpecDocsAction.TASK_GROUP_ID);
        specDocsTask.setDescription(SpecDocsAction.TASK_DESCR);

        final Task oaiDocsTask = project.task(OpenAPIDocsAction.TASK_ID).doLast(openAPIDocsAction);
        oaiDocsTask.setGroup(OpenAPIDocsAction.TASK_GROUP_ID);
        oaiDocsTask.setDescription(OpenAPIDocsAction.TASK_DESCR);
    }
}
