package ru.uip.contract;

import org.gradle.api.file.ConfigurableFileCollection;

import java.io.File;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContractConfigParser {

    private final Set<ContractOperationConfig> contractOperationConfigs;

    public ContractConfigParser(Map<String, ConfigurableFileCollection> config) {
        this.contractOperationConfigs = config.entrySet().stream()
                .map(v -> new ContractOperationConfig(v.getKey(), v.getValue().getFiles()))
                .collect(Collectors.toSet());
    }

    public void parse() {
        for(ContractOperationConfig contractOperationConfig: contractOperationConfigs) {
            final Set<File> contractFiles = contractOperationConfig.files;
            for(File contractFile: contractFiles) {
                parseContractFile(contractFile);
            }
        }
    }

    public void parseContractFile(File contractFile) {
        System.out.println(contractFile.getName());
    }
}
