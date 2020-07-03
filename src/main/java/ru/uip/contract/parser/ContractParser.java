package ru.uip.contract.parser;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.fasterxml.jackson.dataformat.yaml.YAMLParser;

import java.io.IOException;
import java.util.List;

public class ContractParser {

    public void parse(String fileContent) throws IOException {
        YAMLFactory yaml = new YAMLFactory();
        ObjectMapper mapper = new ObjectMapper();

        YAMLParser yamlParser = yaml.createParser(fileContent);
        final List<ObjectNode> docs = mapper
                .readValues(yamlParser, new TypeReference<ObjectNode>() {})
                .readAll();
        for(ObjectNode doc: docs) {
            doc.fields().forEachRemaining(System.out::println);
        }
    }
}
