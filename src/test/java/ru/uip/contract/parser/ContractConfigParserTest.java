package ru.uip.contract.parser;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ContractConfigParserTest {

    private ContractConfigParser parser;

    @BeforeEach
    public void setUp() {
        Map<String, Set<File>> contracts = new HashMap<>();
        File getAccountContract = new File("./src/test/contracts/getAccounts.yml");
        Set<File> contractFiles = new HashSet<>();
        contractFiles.add(getAccountContract);
        contracts.put("GetAccounts", contractFiles);
        this.parser = new ContractConfigParser(contracts);
    }

    @Test
    public void testConstruct() {
        assertThat(parser, notNullValue());
    }

    @Test
    public void testParseFile() {
        File getAccountContract = new File("./src/test/contracts/getAccounts.yml");
        final List<ContractDescription> contractDescriptions = parser.parseContractFile(getAccountContract);
        assertThat(contractDescriptions, containsInAnyOrder(
            new ContractDescription("Should get all accounts", "Get all accounts"),
            new ContractDescription("Should get existing account", "Get account by account number"),
            new ContractDescription("Should get existing account 2", "Get account by account number"),
            new ContractDescription("Should return 404 on get non existing account.", "Return 404 on get non existing account. Response body is empty.")

        ));
    }

    @Test
    public void testParseNonExistedFile() {
        File getAccountContract = new File("./src/test/contracts/nonExists.yml");
        assertThrows(
                IllegalArgumentException.class,
                () ->parser.parseContractFile(getAccountContract));
    }

}
