package com.dac.dac.exption;

import lombok.Getter;

import java.util.Map;

@Getter
public class DuplicatedEntryException extends RuntimeException {
    private final Map<String, String> duplicateFields;

    public DuplicatedEntryException(Map<String, String> duplicateFields) {
        super("Duplicate fields found: " + String.join(", ", duplicateFields.keySet()));
        this.duplicateFields = duplicateFields;
    }

}
