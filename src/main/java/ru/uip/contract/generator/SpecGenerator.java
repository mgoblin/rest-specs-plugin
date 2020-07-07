package ru.uip.contract.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import ru.uip.contract.parser.ContractDescription;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SpecGenerator {

    private final MustacheFactory mf;// = new DefaultMustacheFactory();
    private final Mustache m;

    public SpecGenerator(String templateName) {
        this.mf = new DefaultMustacheFactory();
        this.m = mf.compile(templateName);
    }


    public Map<String, Set<String>> generateSpecs(Map<String, Set<ContractDescription>> operationContracts) {

        Map<String, Set<String>> operationDocs = new HashMap<>();
        for(Map.Entry<String, Set<ContractDescription>> operationContract: operationContracts.entrySet()) {
            final Set<ContractDescription> descriptions = operationContract.getValue();
            final String operationId = operationContract.getKey();

            for(ContractDescription description: descriptions) {
                final String contractDescription = generateContractDescription(description);
                final Set<String> operationDescriptions = operationDocs.getOrDefault(operationId, new HashSet<>());
                operationDescriptions.add(contractDescription);
                operationDocs.put(operationId, operationDescriptions);
            }
        }
        return operationDocs;
    }

    public String generateContractDescription(ContractDescription description) {
        StringWriter writer = new StringWriter();
        m.execute(writer, description);
        return writer.toString();
    }

}
