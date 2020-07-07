package ru.uip.contract.generator;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import ru.uip.contract.parser.ContractDescription;

import java.io.StringWriter;
import java.util.Map;
import java.util.Set;

public class SpecGenerator {

    private MustacheFactory mf = new DefaultMustacheFactory();

    public String generateSpecs(Map<String, Set<ContractDescription>> operationContracts) {

        Mustache m = mf.compile("spec.mustache");

        ContractDescription description = new ContractDescription("Its name", "Its desc");

        StringWriter writer = new StringWriter();
        m.execute(writer, description);
        return writer.toString();
    }

}
