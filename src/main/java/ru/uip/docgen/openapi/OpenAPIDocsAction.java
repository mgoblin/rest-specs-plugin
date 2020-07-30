package ru.uip.docgen.openapi;

import org.gradle.api.Action;
import org.gradle.api.NonNullApi;
import org.gradle.api.Task;

@NonNullApi
public class OpenAPIDocsAction implements Action<Task> {
    @Override
    public void execute(Task task) {
        System.out.println("OpenAPIDocsAction executed");
    }
}
