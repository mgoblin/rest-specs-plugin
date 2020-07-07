package ru.uip.contract.generator;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("unit")
public class AsciidocSpecGeneratorTest {

    private AsciidocSpecGenerator generator = new AsciidocSpecGenerator();

    @Test
    public void testGenerateSpec() {
        final String spec = generator.generateSpec();
        assertThat(spec, equalTo("<h2>Its name</h2>\n<p>Its desc</p>"));
    }
}
