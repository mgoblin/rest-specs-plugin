package ru.uip.contract.plugin;

import lombok.*;
import org.gradle.api.file.ConfigurableFileCollection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class SpecPluginExtension {
    private String apiSpec = "src/main/api/api.yaml";
    private Map<String, ConfigurableFileCollection> operationContracts = new HashMap<>();
}
