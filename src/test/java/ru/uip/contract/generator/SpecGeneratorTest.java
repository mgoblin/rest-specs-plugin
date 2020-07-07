package ru.uip.contract.generator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.contract.parser.ContractDescription;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("unit")
public class SpecGeneratorTest {

    private SpecGenerator generator = new SpecGenerator();

    @Test
    public void testGenerateSpec() {
        Map<String, Set<ContractDescription>> operationContracts = new HashMap<>();
        final String spec = generator.generateSpecs(operationContracts);
        assertThat(spec, equalTo("<h2>Its name</h2>\n<p>Its desc</p>"));
    }
}
