package ru.uip.contract.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import ru.uip.contract.parser.ContractDescription;

import java.io.StringWriter;

public class AsciidocSpecGenerator {

    private MustacheFactory mf = new DefaultMustacheFactory();

    public String generateSpec() {
        Mustache m = mf.compile("index.mustache");

        ContractDescription description = new ContractDescription("Its name", "Its desc");

        StringWriter writer = new StringWriter();
        m.execute(writer, description);
        return writer.toString();
    }
}
