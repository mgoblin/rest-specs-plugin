package ru.uip.contract.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import java.io.IOException;
import java.util.List;

public class ContractParser {

    public void parse(String fileContent) throws IOException {
        YAMLFactory yaml = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper().configure(
                DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);

        YAMLParser yamlParser = yaml.createParser(fileContent);
        final List<ContractDescription> docs = mapper
                .readValues(yamlParser, new TypeReference<ContractDescription>() {})
                .readAll();
        for(ContractDescription description: docs) {
            System.out.println(description);
        }
    }
}
