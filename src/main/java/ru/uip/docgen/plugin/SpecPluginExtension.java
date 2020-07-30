package ru.uip.docgen.plugin;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.gradle.api.file.ConfigurableFileCollection;

import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
public class SpecPluginExtension {
    private String apiSpec = "src/main/api/api.yaml";
    private String generatorClass = "ru.uip.docgen.openapi.generator.impl.AsciidocGenerator";
    private Map<String, ConfigurableFileCollection> operationContracts = new HashMap<>();
    private String template;
    private String snippetsDir;
    private String outputDir;
}
