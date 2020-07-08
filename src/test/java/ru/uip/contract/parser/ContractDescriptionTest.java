package ru.uip.contract.parser;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

@Tag("unit")
public class ContractDescriptionTest {

    @Test
    public void testSpecDirName() {
        ContractDescription contractDescription = new ContractDescription(
                "Should delete account with id equals to 1",
                "Delete existing account balance with id = 1."
        );

        assertThat(contractDescription.getSpecDirName(), equalTo("validate_should_delete_account_with_id_equals_to_1"));
    }
}
