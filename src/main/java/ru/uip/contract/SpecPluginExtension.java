package ru.uip.contract;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@NoArgsConstructor
public class SpecPluginExtension {
    private String apiSpec = "src/main/api/api.yaml";
}
