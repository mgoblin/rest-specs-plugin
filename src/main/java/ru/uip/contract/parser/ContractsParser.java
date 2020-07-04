package ru.uip.contract.parser;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class ContractsParser {

    private final Set<ContractOperationConfig> contractOperationConfigs;
    private final ContractContentParser contractContentParser;

    public ContractsParser(Map<String, Set<File>> config) {
        this(config, new ContractContentParser());
    }

    public ContractsParser(Map<String, Set<File>> config, ContractContentParser contractContentParser) {
        this.contractOperationConfigs = config.entrySet().stream()
                .map(v -> new ContractOperationConfig(v.getKey(), v.getValue()))
                .collect(Collectors.toSet());
        this.contractContentParser = contractContentParser;
    }

    public void parse() {
        for(ContractOperationConfig contractOperationConfig: contractOperationConfigs) {
            final Set<File> contractFiles = contractOperationConfig.files;
            for(File contractFile: contractFiles) {
                parseContractFile(contractFile);
            }
        }
    }

    public Set<ContractDescription> parseContractFile(File contractFile) {
        try {
            final String fileContent = Files.readString(contractFile.toPath(), StandardCharsets.UTF_8);
            return contractContentParser.parse(fileContent);
        } catch (IOException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
