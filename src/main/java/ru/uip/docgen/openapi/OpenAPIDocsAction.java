package ru.uip.docgen.openapi;

import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Task;

@NonNullApi
public class OpenAPIDocsAction implements Action<Task> {

    public static final String TASK_ID = "contract-gen-spec";
    public static final String TASK_GROUP_ID = "documentation";
    public static final String TASK_DESCR = "Generate REST service specification from open api spec and contract tests";

    @Override
    public void execute(Task task) {
        System.out.println("OpenAPIDocsAction executed");
    }
}
