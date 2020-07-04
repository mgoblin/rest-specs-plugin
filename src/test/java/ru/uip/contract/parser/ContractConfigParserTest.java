package ru.uip.contract.parser;

import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@Tag("unit")
public class ContractConfigParserTest {

    private Map<String, Set<File>> contracts = new HashMap<>();

    @BeforeEach
    public void setUp() {
        File getAccountContract = new File("./src/test/contracts/getAccounts.yml");
        Set<File> contractFiles = new HashSet<>();
        contractFiles.add(getAccountContract);
        contracts.put("GetAccounts", contractFiles);
    }

    @Test
    public void testConstruct() {
        final ContractConfigParser parser = new ContractConfigParser(contracts);
        assertThat(parser, notNullValue());
    }

    @SneakyThrows
    @Test
    public void testParseFile() {
        final ContractParser contractParser = mock(ContractParser.class);
        final ContractConfigParser parser = new ContractConfigParser(contracts, contractParser);

        final ContractDescription item = new ContractDescription("accounts", "Get all accounts");
        List<ContractDescription> expected = Arrays.asList(item);
        when(contractParser.parse(anyString())).thenReturn(expected);

        File getAccountContract = new File("./src/test/contracts/getAccounts.yml");
        final List<ContractDescription> contractDescriptions = parser.parseContractFile(getAccountContract);
        assertThat(contractDescriptions, containsInAnyOrder(item));

        verify(contractParser, times(1)).parse(anyString());
    }

    @SneakyThrows
    @Test
    public void testParseNonExistedFile() {
        final ContractParser contractParser = mock(ContractParser.class);
        final ContractConfigParser parser = new ContractConfigParser(contracts, contractParser);

        File getAccountContract = new File("./src/test/contracts/nonExists.yml");
        assertThrows(
                IllegalArgumentException.class,
                () -> parser.parseContractFile(getAccountContract));

        verify(contractParser, times(0)).parse(anyString());
    }

}
