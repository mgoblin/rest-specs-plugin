package ru.uip.docgen.openapi;

import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.docgen.openapi.parser.OpenApiParser;

import java.util.List;

@Tag("unit")
public class OpenApiParserTest {

    @Test
    public void testHappyPath() {
        OpenApiParser parser = new OpenApiParser("./src/test/api/api.yaml");
        MatcherAssert.assertThat(parser, Matchers.notNullValue());

        final List<String> operations = parser.parseOperationIds();
        MatcherAssert.assertThat(operations, Matchers.hasItems(
                "GetAccounts",
                "CreateOrUpdateAccount",
                "FindAccountById",
                "DeleteAccount"
        ));
    }

    @Test
    public void testNonExistingFile() {
        Assertions.assertThrows(
                IllegalArgumentException.class,
                () -> new OpenApiParser("./src/test/api/no.file")
        );
    }
}
