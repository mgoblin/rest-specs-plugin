package ru.uip.contract.parser;

import java.io.File;
import java.util.Set;

public class ContractOperationConfig {

    public final String operationId;
    public final Set<File> files;

    public ContractOperationConfig(String operationId, Set<File> files) {
        this.operationId = operationId;
        this.files = files;
    }
}
