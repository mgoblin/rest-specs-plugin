package ru.uip.contract;

import org.gradle.api.Plugin;
import org.gradle.api.Project;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public class SpecPlugin implements Plugin<Project> {

    @Override
    public void apply(Project project) {
        final SpecPluginExtension apiExt = project.getExtensions().create("apiContractSpec", SpecPluginExtension.class);

        project.task("generate-api-spec").doLast(task -> {
            final OpenApiParser openApiParser = new OpenApiParser(apiExt.getApiSpec(), project);
            openApiParser.parseOperationIds().forEach(System.out::println);

//            Path dir = FileSystems.getDefault().getPath("/home/mike/IdeaProjects/gradle_contracts/producer/src/test/resources/contracts");
//            try(DirectoryStream<Path> stream = Files.newDirectoryStream( dir, "*.yml" )) {
//                for (Path path : stream) {
//                    System.out.println( path.getFileName() );
//                }
//            } catch (IOException ex) {
//                ex.printStackTrace();
//            }

        });

    }

}
