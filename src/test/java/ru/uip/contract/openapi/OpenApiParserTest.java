package ru.uip.contract.openapi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import ru.uip.openapi.OpenApiParser;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.notNullValue;

@Tag("unit")
public class OpenApiParserTest {

    @Test
    public void testHappyPath() {
        OpenApiParser parser = new OpenApiParser("./src/test/api/api.yaml");
        assertThat(parser, notNullValue());

        final List<String> operations = parser.parseOperationIds();
        assertThat(operations, hasItems(
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
