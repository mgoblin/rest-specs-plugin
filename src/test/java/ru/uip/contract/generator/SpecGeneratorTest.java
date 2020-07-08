package ru.uip.contract.generator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.contract.parser.ContractDescription;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("unit")
public class SpecGeneratorTest {

    private SpecGenerator generator = new SpecGenerator("spec.mustache");

    @Test
    public void testGenerateSpec() {
        Map<String, Set<ContractDescription>> operationContracts = new HashMap<>();
        ContractDescription description = new ContractDescription("Its name", "Its desc");
        Set<ContractDescription> contractDescriptions = new HashSet<>();
        contractDescriptions.add(description);
        operationContracts.put("Test", contractDescriptions);

        final Map<String, String> spec = generator.generateSpecs(operationContracts);
        final String separator = System.getProperty("line.separator");
        final String expected = spec.get("Test").replaceAll(separator, "");

        assertThat(expected, equalTo("===== Its nameIts desc"));
    }
}
