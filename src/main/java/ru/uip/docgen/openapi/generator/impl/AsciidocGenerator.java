package ru.uip.docgen.openapi.generator.impl;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import io.swagger.v3.oas.models.OpenAPI;
import lombok.Getter;
import lombok.Setter;
import ru.uip.docgen.openapi.generator.spi.APISpecGenerator;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Getter
@Setter
public class AsciidocGenerator implements APISpecGenerator {

    @Override
    public String generateSpec(OpenAPI openAPI) {
        try(StringWriter writer = new StringWriter();
            InputStream stream = this.getClass().getResourceAsStream("/index.mustache");
            InputStreamReader reader = new InputStreamReader(stream)) {

            final MustacheFactory mf = new DefaultMustacheFactory();
            final Mustache m = mf.compile(reader, "index.mustache");
            m.execute(writer, createContext(openAPI));

            return writer.toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<String, Object> createContext(OpenAPI openAPI) {
        Map<String, Object> context = new HashMap<>();
        context.put("appName", openAPI.getInfo().getTitle());
        return context;
    }
}
