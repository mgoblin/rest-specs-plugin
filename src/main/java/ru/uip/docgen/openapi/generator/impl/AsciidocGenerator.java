package ru.uip.docgen.openapi.generator.impl;

import ru.uip.docgen.openapi.generator.spi.APISpecGenerator;

public class AsciidocGenerator implements APISpecGenerator {
    @Override
    public String generateSpec() {
        return "asciidoc";
    }
}
