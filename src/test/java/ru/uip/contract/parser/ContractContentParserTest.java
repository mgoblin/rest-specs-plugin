package ru.uip.contract.parser;

import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Tag("unit")
public class ContractContentParserTest {

    private ContractContentParser parser = new ContractContentParser();

    @Test
    public void testEmptyContent() throws IOException {
        final Set<ContractDescription> descriptions = parser.parse("");
        assertThat(descriptions, hasSize(0));
    }

    @Test
    public void testHappyPath() throws IOException {
        Path filePath = Paths.get("./src/test/contracts/getAccounts.yml");
        final String fileContent = Files.readString(filePath);

        final Set<ContractDescription> descriptions = parser.parse(fileContent);
        assertThat(descriptions, containsInAnyOrder(
                new ContractDescription("Should get all accounts", "Get all accounts"),
                new ContractDescription("Should return 404 on get non existing account.", "Return 404 on get non existing account. Response body is empty."),
                new ContractDescription("Should get existing account", "Get account by account number"),
                new ContractDescription("Should get existing account 2", "Get account by account number")
        ));
    }

    @Test
    public void testIncorrectFormat() {
        assertThrows(
                MismatchedInputException.class,
                () -> parser.parse("incorrect yaml"));
    }
}
