package ru.uip.contract.parser;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode
@ToString
public class ContractDescription {
    private final String name;
    private final String description;

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public ContractDescription(
            @JsonProperty("name") String name,
            @JsonProperty("description") String description) {
        this.name = name;
        this.description = description;
    }
}
